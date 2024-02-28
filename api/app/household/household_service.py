import os
from pymongo import MongoClient
from bson import ObjectId
from app.parsers import *

class HouseholdService:
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.household_collection = self.db[household_collection_name]
        
    def get_all(self):
        households = self.household_collection.find()
        
        return households