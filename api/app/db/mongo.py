import os
from app.db.app_database import AppDatabase
from pymongo import MongoClient
from pymongo.database import Database
from pymongo.collection import Collection


class MongoDatabase(AppDatabase):
    def __init__(self):
        self.mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        self.db_name = os.getenv("MONGODB_DATABASE_NAME")
        self.user_collection_name = os.getenv("MONGODB_COLLECTION_USERS")
        self.household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        
        # self.client: MongoClient = MongoClient(mongodb_url)
        # self.db: MongoDatabase = self.client[db_name]
        # self.user_collection: Collection = self.db[user_collection_name]
        # self.household_collection: Collection = self.db[household_collection_name]

    @property
    def client(self) -> MongoClient:
        return MongoClient(self.mongodb_url)
    
    @property
    def db(self) -> Database:
        return self.client[self.db_name]
    
    @property
    def user_collection(self) -> Collection:
        return self.db[self.user_collection_name]
    
    @property
    def household_collection(self) -> Collection:
        return self.db[self.household_collection_name]
