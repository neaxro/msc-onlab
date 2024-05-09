from app.db.app_database import AppDatabase
from app.db.mongo import MongoDatabase
from app.utils.parsers import *

class RegisterService:
    def __init__(self, database: AppDatabase = MongoDatabase()):
        self.database = database

    def validate_json_format(self, json_data):
        required_fields = ["first_name", "last_name", "username", "email", "password"]

        for field in required_fields:
            if not json_data.get(field):
                raise Exception(f"Field '{field}' is required and cannot be empty.")
