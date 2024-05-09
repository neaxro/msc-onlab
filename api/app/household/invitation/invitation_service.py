import os, jwt
from datetime import timedelta, datetime
from app.utils.parsers import *
from app.utils.time_management import *
from app.user.user_service import UserService
from app.household.household_service import HouseholdService
from app.utils.templater import Templater
from app.db.app_database import AppDatabase
from app.db.mongo import MongoDatabase

class InvitationService:
    def __init__(self, database: AppDatabase = MongoDatabase()):
        self.database = database
        self.user_service = UserService(database)
        self.household_service = HouseholdService(database)
        self.templater = Templater()
    
    def _create_invitation_token(self,
            sender_id,
            household_id,
            invited_user_id,
            email_address
        ):
        
        expiration_time_hours = 48
        secret_key = os.environ['TOKEN_SECRET_KEY']
        
        expiration_time = datetime.utcnow() + timedelta(hours=expiration_time_hours)
        token = jwt.encode(
            {
                'sender_id': sender_id,
                'household_id': household_id,
                'invited_user_id': invited_user_id,
                'email_address': email_address,
                'exp': expiration_time
            },
            secret_key,
            algorithm='HS256'
        )
        
        return token
    
    def send_invitations(self, invitation_data):
        sender_id = invitation_data['sender_id']
        household_id = invitation_data['household_id']
        invited_users = invitation_data['invited_users']
        
        # TODO: Send emails to the given email addresses
        for index, user in enumerate(invited_users):
            user_id = user['user_id']
            email = user['email']
            
            invitation_token = self._create_invitation_token(
                sender_id=sender_id,
                household_id=household_id,
                email_address=email,
                invited_user_id=user_id
            )
            
            print('#######################################################')
            print(f"{index+1}.\t{invitation_token}")
            print('#######################################################')
    
    def _unwrap_token(self, invitation_token):
        secret_key = os.environ['TOKEN_SECRET_KEY']
        data = jwt.decode(invitation_token, secret_key, algorithms=['HS256'])
        return data
    
    def process_invitation(self, user_token_data, invitation_token):
        
        # Get invitation token's data        
        try:
            invitation_token_data = self._unwrap_token(invitation_token)
        except jwt.ExpiredSignatureError:
            raise Exception("Invitation token has expired")
        except jwt.InvalidTokenError:
            raise Exception("Invitation token is invalid")
        
        user_id=invitation_token_data['invited_user_id']
        household_id=invitation_token_data['household_id']
        
        # Raise exception is user is not invited
        if user_token_data['id'] != invitation_token_data['invited_user_id']:
            raise Exception("Current user is not present in the invited users")

        users_in_household = self.household_service.get_user_ids_in_household(household_id)
        
        if user_id in users_in_household:
            raise Exception("User is already in household")
        
        result = self.household_service.insert_user_to_household(
            household_id=household_id,
            user_id=user_id
        )
        
        return result