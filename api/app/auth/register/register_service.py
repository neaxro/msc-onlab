import os
from pymongo import MongoClient
from bson import ObjectId
from app.parsers import *

class RegisterService:
    def __init__(self):
        self.mongodb = os.getenv("MONGODB_CONNECTION_URL")
        self.client = MongoClient(self.mongodb)
        self.db = self.client['msc_onlab']
        self.user_collection = self.db['users']
        
    def validate_json_format(self, json_data):
        required_fields = ["first_name", "last_name", "username", "email", "password"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is required and cannot be empty.")
