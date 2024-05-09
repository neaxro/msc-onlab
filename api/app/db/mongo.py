import os
from app.db.app_database import AppDatabase
from pymongo import MongoClient


class MongoDatabase(AppDatabase):
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        user_collection_name = os.getenv("MONGODB_COLLECTION_USERS")
        household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.user_collection = self.db[user_collection_name]
        self.household_collection = self.db[household_collection_name]
