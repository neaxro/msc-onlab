import os
from pymongo import MongoClient
from app.parsers import *

class LoginService:
    def __init__(self):
        self.mongodb = os.getenv("MONGODB_CONNECTION_URL")
        self.client = MongoClient(self.mongodb)
        self.login_db = self.client['proba']
        self.login_collection = self.login_db['ember']
        
    def get_data(self):
        documents = list(
            self.login_collection.find()
        )
        return parse_json(documents)
