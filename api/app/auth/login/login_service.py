import os, datetime, jwt
from app.db.app_database import AppDatabase
from app.db.mongo import MongoDatabase
from app.utils.parsers import *

from app.user.user_service import UserService

class LoginService:
    def __init__(self, database: AppDatabase = MongoDatabase()):
        self.database = database
        self.user_service = UserService(database)
    
    def validate_json_format(self, json_data):
        required_fields = ["username", "password"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is required and cannot be empty.")
    
    def _generate_token(self, username, id):
        expiration_time_hours = 2
        secret_key = os.environ['TOKEN_SECRET_KEY']
            
        expiration_time = datetime.datetime.utcnow() + datetime.timedelta(hours=expiration_time_hours)
        token = jwt.encode(
            {
                'id': id,
                'username': username,
                'exp': expiration_time
            },
            secret_key,
            algorithm='HS256'
        )
        
        return token
    
    def login_user(self, json_data: json) -> dict:
        username = json_data.get('username')
        password = json_data.get('password')
        
        user = self.user_service.get_user_by_username(username)
        id = user.get('_id', {}).get('$oid')
        
        if user is None:
            raise Exception("User does not exist!")

        if user['password'] == password:
            token = self._generate_token(username, id)
            return token
        
        else:
            raise Exception("Password do not mach!")
