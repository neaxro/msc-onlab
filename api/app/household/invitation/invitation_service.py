import os, jwt
from datetime import timedelta, datetime, timezone
from pymongo import MongoClient
from bson import ObjectId
from app.utils.parsers import *
from app.utils.time_management import *
from app.utils.validators import validate_non_empty_array
from app.user.user_service import UserService
from app.household.household_service import HouseholdService
from app.utils.templater import Templater

class InvitationService:
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        invitation_collection_name = os.getenv("MONGODB_COLLECTION_INVITATIONS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.invitation_collection = self.db[invitation_collection_name]
        
        self.templater = Templater()
        self.user_service = UserService()
        self.household_service = HouseholdService()
    
    def get_invitation_by_id(self, invitation_id):
        try:
            invitation = self.invitation_collection.find_one({
                '_id': ObjectId(invitation_id)
            })
            
            return invitation

        except Exception:
            raise Exception(f"Invitation does not exist with ID: {invitation_id}")
    
    def create_invitation(self, invitation_data):
        try:
            # Validate the necessary fields
            sender_id = invitation_data.get('sender_id')
            household_id = invitation_data.get('household_id')
            invited_user_name = invitation_data.get('invited_user_name')
            
            invited_user = self.user_service.get_user_by_username(invited_user_name)
            
            if not invited_user:
                raise Exception(f"User with \'{invited_user_name}\' username does not exist!")
            
            invited_user_id = invited_user['_id']['$oid']
            
            users_in_household = self.household_service.get_user_ids_in_household(household_id)
        
            if ObjectId(invited_user_id) in users_in_household:
                raise Exception("User is already in household")

            if not sender_id or not household_id or not invited_user_name:
                raise ValueError("Missing required fields: sender_id, household_id, or invited_user_id")
            
            # Calculate expiration time (7 days from now)
            expiration_time = datetime.now(timezone.utc) + timedelta(hours=168)
            
            # Insert invitation into the collection
            self.invitation_collection.insert_one({
                'sender_user_id': ObjectId(sender_id),
                'household_id': ObjectId(household_id),
                'invited_user_id': ObjectId(invited_user_id),
                'expiration_date': expiration_time.timestamp(),
                'status': "pending"
            })

        except Exception as e:
            raise Exception(f"Failed to create invitation: {str(e)}")
    
    def get_available_invitations(self, user_id):
        # Fetch the user by ID
        user = self.user_service.get_user_by_id(user_id)
        
        if user is None:
            raise Exception("User does not exist!")
        
        # Get the current UTC timestamp
        current_date = datetime.now(timezone.utc).timestamp()
        
        # Find all unexpired invitations for the user
        invitations = self.invitation_collection.find({
            "invited_user_id": ObjectId(user_id),
            "expiration_date": {"$gt": current_date},  # Assuming an expiration field exists
            "status": "pending"
        })

        # List to store processed invitations with household and sender data
        available_invitations = []

        for invitation in invitations:
            # Fetch household data using household_id
            household = self.household_service.get_household_by_id(invitation["household_id"])

            if household is None:
                raise Exception(f"Household {invitation['household_id']} does not exist!")

            # Fetch sender (user) data using sender_user_id
            sender_user = self.user_service.get_user_by_id(invitation["sender_user_id"])

            if sender_user is None:
                raise Exception(f"Sender user {invitation['sender_user_id']} does not exist!")

            # Build the invitation object with household name and sender data
            available_invitations.append({
                "invitation_id": str(invitation["_id"]),
                "household_name": household["title"],
                "sender": {
                    "_id": sender_user["_id"],
                    "first_name": sender_user["first_name"],
                    "last_name": sender_user["last_name"],
                    "username": sender_user["username"],
                    "email": sender_user["email"],
                    "profile_picture": sender_user.get("profile_picture", "default")
                },
                "invited_user_id": str(invitation["invited_user_id"]),
                "expiration_date": invitation.get("expiration_date", None)
            })
        
        return available_invitations

    
    def accept_invitation(self, user_token_data, invitation_id):
        
        invitation = self.get_invitation_by_id(invitation_id)
        
        invited_user_id = invitation['invited_user_id']
        household_id = invitation['household_id']
        
        # Raise exception is user is not invited
        if str(user_token_data['id']) != str(invited_user_id):
            raise Exception("Current user is not present in the invited users")

        users_in_household = self.household_service.get_user_ids_in_household(household_id)
        
        if ObjectId(invited_user_id) in users_in_household:
            raise Exception("User is already in household")
        
        result = self.household_service.insert_user_to_household(
            household_id=household_id,
            user_id=invited_user_id
        )
        
        self.invitation_collection.update_one(
            {
                "_id": ObjectId(invitation_id)
            },
            {
                '$set': {'status': 'accepted'}
            }
        )
        
        return result

    def decline_invitation(self, user_token_data, invitation_id):
        
        invitation = self.get_invitation_by_id(invitation_id)
        invited_user_id = invitation['invited_user_id']
        household_id = invitation['household_id']
        
        # Raise exception is user is not invited
        if str(user_token_data['id']) != str(invited_user_id):
            raise Exception("Current user is not present in the invited users")
        
        result = self.invitation_collection.update_one(
            {
                "_id": ObjectId(invitation_id)
            },
            {
                '$set': {'status': 'declined'}
            }
        )
        
        return result
