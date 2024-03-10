import os, datetime
from pymongo import MongoClient
from bson import ObjectId
from app.utils.parsers import *
from app.utils.time_management import *
from app.utils.validators import validate_non_empty_array
from app.utils.templater import Templater

from app.household.household_service import HouseholdService

class TaskService:
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.household_collection = self.db[household_collection_name]
        
        self.templater = Templater()
        self.household_service = HouseholdService()
    
    def validate_json_format_insert(self, json_data):
        required_fields = ["title", "description", "due_date"]

        for field in required_fields:
            if field not in json_data or not json_data[field]:
                raise Exception(f"Field '{field}' is missing or empty in the JSON data.")

    def insert_task(self, household_id, task_data: json):    
        task_data['oid'] = str(ObjectId())
                    
        new_task = self.templater.get_task_template(task_data)
        
        result = self.household_service.insert_task_to_household(household_id, new_task)
        return result

    def get_all(self, houeshold_id):
        tasks = self.household_collection.find_one(
            {
                "_id": ObjectId(houeshold_id)
            },
            {
                "tasks": 1,
                "_id": 0
            }
        )
        
        return parse_json(tasks.get("tasks"))
    
    def get_all_brief(self, houeshold_id):
        tasks = self.household_collection.find_one(
            {
                "_id": ObjectId(houeshold_id)
            },
            {
                "tasks": 1,
            }
        ).get("tasks")
        
        transformed_tasks = self.templater.get_brief_task_list(tasks)
        
        return parse_json(transformed_tasks)