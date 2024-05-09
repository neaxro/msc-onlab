from bson import ObjectId

class TestUser:
    def __init__(self, name, user_id):
        self.name = name
        self.user_id = user_id
    
    def get_json(self):
        return {
            "_id": ObjectId(self.user_id),
            "first_name": self.name,
            "last_name": "Test",
            "username": self.name.lower(),
            "email": f"{self.name.lower()}.test@gmail.com",
            "profile_picture": "default",
            "password": "testpassword123456"
        }

class TestHousehold:
    def __init__(self, household_id, title, creation_date, people, tasks):
        self.household_id = household_id
        self.title = title
        self.creation_date = creation_date
        self.people = people
        self.tasks = tasks
    
    def get_json(self):
        return {
            "_id": ObjectId(self.household_id),
            "title": self.title,
            "creation_date": f"{self.creation_date} 20:06:22",
            "people": self.people,
            "tasks": self.tasks
        }

class TestTask:
    def __init__(self, task_id, title, creation_date, description, done, due_date, responsible_id, subtasks):
        self.task_id = task_id
        self.title = title
        self.creation_date = creation_date
        self.description = description
        self.done = done
        self.due_date = due_date
        self.responsible_id = responsible_id
        self.subtasks = subtasks
    
    def get_json(self):
        
        
        return {
            "creation_date": f"{self.creation_date} 16:05:18",
            "description": self.description,
            "done": self.done,
            "due_date": self.due_date,
            "responsible_id": ObjectId(self.responsible_id),
            "subtasks": self.subtasks,
            "title": self.title,
            "_id": ObjectId(self.task_id)
        }

class TestSubtask:
    def __init__(self, subtask_id, title, done, type):
        self.subtask_id = subtask_id
        self.title = title
        self.done = done
        self.type = type

    def get_json(self):
        return {
            "_id": ObjectId(self.subtask_id),
            "done": self.done,
            "title": self.title,
            "type": self.type
        }
