from bson import ObjectId
from app.utils.parsers import *
from app.db.app_database import AppDatabase
from app.db.mongo import MongoDatabase

class UserService:
    def __init__(self, database: AppDatabase = MongoDatabase()):
        self.database = database
            
    def validate_json_format(self, json_data):
        required_fields = ["_id", "first_name", "last_name", "username", "email", "password"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is missing in the JSON data.")
      
    def get_user_by_username(self, username: str) -> json:
        user = self.database.user_collection.find_one({"username": username})
        return parse_json(user)
    
    def get_user_by_id(self, id: str) -> json:
        user = self.database.user_collection.find_one({"_id": ObjectId(id)})
        return parse_json(user)
    
    def insert_user(self, data: json):
        existing_user = self.get_user_by_username(data['username'])
        
        if existing_user is None:
            result = self.database.user_collection.insert_one(data)
            return result

        else:
            raise Exception('User already exists!')

    def replace_user(self, data: json):
        id = data.get('_id', {}).get('$oid')
        
        if not id:
            raise ValueError('Invalid data format: _id is required.')

        user = self.get_user_by_id(id)

        if user is None:
            raise Exception('User does not exist!')
        
        data.pop('_id', None)
        
        result = self.database.user_collection.replace_one(
            filter={"_id": ObjectId(id)},
            replacement=data
        )
        
        return result