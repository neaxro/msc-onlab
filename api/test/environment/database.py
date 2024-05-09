import os, mongomock
from app.db.app_database import AppDatabase
from pymongo import MongoClient
from pymongo.database import Database
from pymongo.collection import Collection
from test.environment.classes import *

class MockedDatabase(AppDatabase):
    def __init__(self):
        self.db_name = os.getenv("MONGODB_TEST_DATABASE_NAME")
        self.household_collection_name = os.getenv("MONGODB_COLLECTION_HOUSEHOLDS")
        self.user_collection_name = os.getenv("MONGODB_COLLECTION_USERS")
        
        self.setup_all()
    
    @property
    def client(self) -> MongoClient:
        return mongomock.MongoClient()
    
    @property
    def db(self) -> Database:
        return self.client[self.db_name]
    
    @property
    def user_collection(self) -> Collection:
        return self.db[self.user_collection_name]
    
    @property
    def household_collection(self) -> Collection:
        return self.db[self.household_collection_name]

    def setup_users(self):
        """Creates test users in the users collection in the test database.
        """
        
        test_user_1 = TestUser("Alice", "65dcf20484273f26b75734f0")
        test_user_2 = TestUser("Bob", "65dcf20484273f26b75734f1")
        test_user_3 = TestUser("Emma", "65dcf20484273f26b75734f2")
        test_user_4 = TestUser("William", "65dcf20484273f26b75734f3")
        
        self.user_collection.insert_one(test_user_1.get_json())
        self.user_collection.insert_one(test_user_2.get_json())
        self.user_collection.insert_one(test_user_3.get_json())
        self.user_collection.insert_one(test_user_4.get_json())
    
    def teardown_users(self):
        """Removes the user collection from the test database.
        """
        self.user_collection.drop()
    
    def setup_households(self):
        """Creates test households in the users collection in the test database.
        """
        
        test_subtask_1 = TestSubtask(subtask_id="65dcf20484273f26b75734b0",title="Plates",done=False,type="checkbox")
        test_subtask_2 = TestSubtask(subtask_id="65dcf20484273f26b75734b1",title="Oranges",done=False,type="checkbox")
        test_subtask_3 = TestSubtask(subtask_id="65dcf20484273f26b75734b2",title="Apple",done=False,type="checkbox")
        test_subtask_4 = TestSubtask(subtask_id="65dcf20484273f26b75734b3",title="Banana",done=True,type="checkbox")
        
        test_task_1 = TestTask(task_id="65dcf20484273f26b75734a0",title="Wash the dishes",creation_date="2024.05.08.",description="Test Description",done=False,due_date="2024.05.20.",responsible_id="65dcf20484273f26b75734f0",subtasks=[test_subtask_1.get_json()])
        test_task_2 = TestTask(task_id="65dcf20484273f26b75734a1",title="Feed the dog",creation_date="2024.05.07.",description="Test Description",done=False,due_date="2024.05.21.",responsible_id="65dcf20484273f26b75734f1",subtasks=[])
        test_task_3 = TestTask(task_id="65dcf20484273f26b75734a2",title="Clean the window",creation_date="2024.05.06.",description="Test Description",done=False,due_date="2024.05.14.",responsible_id="65dcf20484273f26b75734f1",subtasks=[])
        test_task_4 = TestTask(task_id="65dcf20484273f26b75734a3",title="Tidy up the room",creation_date="2024.04.01.",description="Test Description",done=False,due_date="2024.05.12.",responsible_id="65dcf20484273f26b75734f0",subtasks=[])
        test_task_5 = TestTask(task_id="65dcf20484273f26b75734a4",title="Vacuum the floor",creation_date="2024.05.07.",description="Test Description",done=False,due_date="2024.05.14.",responsible_id="65dcf20484273f26b75734f1",subtasks=[])
        test_task_6 = TestTask(task_id="65dcf20484273f26b75734a5",title="Mop the floor",creation_date="2024.05.06.",description="Test Description",done=True,due_date="2024.05.20.",responsible_id="65dcf20484273f26b75734f0",subtasks=[])
        test_task_7 = TestTask(task_id="65dcf20484273f26b75734a6",title="Do the cooking",creation_date="2024.04.01.",description="Test Description",done=True,due_date="2024.04.12.",responsible_id="65dcf20484273f26b75734f1",subtasks=[])
        test_task_8 = TestTask(task_id="65dcf20484273f26b75734a7",title="Sweep the floor",creation_date="2024.05.07.",description="Test Description",done=True,due_date="2024.05.08.",responsible_id="65dcf20484273f26b75734f2",subtasks=[])
        test_task_9 = TestTask(task_id="65dcf20484273f26b75734a8",title="Clean the kitchen",creation_date="2024.05.06.",description="Test Description",done=True,due_date="2024.05.30.",responsible_id="65dcf20484273f26b75734f1",subtasks=[])
        test_task_10 = TestTask(task_id="65dcf20484273f26b75734a9",title="Do the shopping",creation_date="2024.04.01.",description="Test Description",done=True,due_date="2024.04.02.",responsible_id="65dcf20484273f26b75734f2",subtasks=[test_subtask_2.get_json(), test_subtask_3.get_json(), test_subtask_4.get_json()])
        
        test_household_1 = TestHousehold(household_id = "65dcf20484273f26b75734b0",title = "Test Household 1",creation_date = "2024.01.01.",people = ["65dcf20484273f26b75734f0","65dcf20484273f26b75734f1"],tasks = [test_task_1.get_json(),test_task_4.get_json(),test_task_6.get_json(),test_task_2.get_json(),test_task_5.get_json(),test_task_7.get_json(),test_task_9.get_json()])
        test_household_2 = TestHousehold(household_id = "65dcf20484273f26b75734b1",title = "Test Household 2",creation_date = "2024.02.03.",people = ["65dcf20484273f26b75734f2","65dcf20484273f26b75734f3"],tasks = [test_task_3.get_json(),test_task_8.get_json(),test_task_10.get_json()])
        
        self.household_collection.insert_one(test_household_1.get_json())
        self.household_collection.insert_one(test_household_2.get_json())

    def teardown_households(self):
        """Removes the household collection from the test database.
        """
        self.household_collection.drop()
    
    def setup_all(self):
        """Sets up the database
        """
        self.setup_users()
        self.setup_households()
    
    def teardown_all(self):
        """Removes the collection in the database
        """
        self.teardown_households()
        self.teardown_users()
