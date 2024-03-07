import os, datetime
from pymongo import MongoClient
from bson import ObjectId
from app.utils.parsers import *
from app.utils.time_management import *

class TaskService:
    def __init__(self):
        mongodb_url = os.getenv("MONGODB_CONNECTION_URL")
        db_name = os.getenv("MONGODB_DATABASE_NAME")
        task_collection_name = os.getenv("MONGODB_COLLECTION_TASKS")
        
        self.client = MongoClient(mongodb_url)
        self.db = self.client[db_name]
        self.task_collection = self.db[task_collection_name]