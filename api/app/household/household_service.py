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
        
        return parse_json(households)
    
    def get_all_brief(self):
        pipeline = [
            {
                "$project": {
                    "_id": 1,
                    "title": 1,
                    "no_people": { "$size": "$people" },
                    "no_active_tasks": { "$size": { "$filter": { "input": "$tasks", "as": "task", "cond": { "$eq": ["$$task.done", False] } } } }
                }
            }
        ]
        
        households = self.household_collection.aggregate(pipeline)
        
        return parse_json(households)
    
    def get_by_id(self, id):
        household = self.household_collection.find({'_id': ObjectId(id)})
        
        return parse_json(household)