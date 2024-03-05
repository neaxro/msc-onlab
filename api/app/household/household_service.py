import os, datetime
from pymongo import MongoClient
from bson import ObjectId
from app.utils.parsers import *
from app.utils.time_management import *

class HouseholdService:
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.household_collection = self.db[household_collection_name]
    
    def validate_json_format(self, json_data):
        required_fields = ["_id", "title", "creation_date", "people", "tasks"]

        for field in required_fields:
            if field not in json_data or not json_data[field]:
                raise Exception(f"Field '{field}' is missing or empty in the JSON data.")

        if "people" in json_data:
            self._validate_non_empty_array(json_data["people"], "people")
        if "tasks" in json_data:
            self._validate_non_empty_array(json_data["tasks"], "tasks")

    def _validate_non_empty_array(self, array, field_name):
        if not array or not all(item for item in array):
            raise Exception(f"Field '{field_name}' must be a non-empty array with non-empty elements.")
    
    def validate_json_format_modify(self, json_data):
        required_fields = ["title"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is missing in the JSON data.")
    
    def validate_json_format_insert(self, json_data):
        required_fields = ["title"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is missing in the JSON data.")
    
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
        household = self.household_collection.find_one({'_id': ObjectId(id)})
        
        return parse_json(household)
    
    def insert_household(self, household_data: json):        
        new_household = {
            "title": household_data['title'],
            "creation_date": utcnow(),
            "people": [],
            "tasks": []
        }
        
        result = self.household_collection.insert_one(new_household)
        return result
    
    def replace_household(self, id: str, household_data: json):

        household = self.get_by_id(id)
        
        if household is None:
            raise Exception('Household does not exist!')
                
        household.pop('_id', None)
        
        household['title'] = household_data['title']
            
        result = self.household_collection.replace_one(
            filter={"_id": ObjectId(id)},
            replacement=dict(household)
        )
        
        return result
    
    def delete_household(self, id: str):
        result = self.household_collection.delete_one({'_id': ObjectId(id)})
        
        return result