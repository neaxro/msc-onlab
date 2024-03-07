from jinja2 import Template, Environment, FileSystemLoader
from datetime import datetime
import json, os
from app.utils.time_management import *
from app.utils.parsers import *

class Templater:
    def __init__(self):
        self.app_folder = os.environ["APP_FOLDER_PATH"]
        self.template_folder_path = os.path.join(self.app_folder, 'templates')

    def _load_template(self, name: str) -> Template:
        template_path = os.path.join(self.template_folder_path, name)
        
        with open(template_path, 'r') as f:
            template = f.read()
        
        return Template(template)

    def get_basic_succes_template(self, status: str, data: json) -> json:
        template_name = "basic_success_reply.j2"
        template = self._load_template(template_name)
        
        values = {
            "status": status,
            "data": data,
            "datetime": utcnow()
        }
        
        return template.render(values)
    
    def get_basic_error_template(self, error_message: str) -> json:
        template_name = "basic_error_reply.j2"
        template = self._load_template(template_name)
        
        values = {
            "error": error_message,
            "datetime": utcnow()
        }
        
        return template.render(values)
    
    def get_task_template(self, task_data) -> dict:
        template_name = "task.j2"
        template = self._load_template(template_name)
        
        values = {
            "title": task_data['title'],
            "description": task_data['description'],
            "creation_datetime": utcnow(),
            "due_date": task_data['due_date'],
            "responsible_id": task_data['responsible_id'],
            "subtasks": task_data['subtasks']
        }
        
        t = template.render(values)
        print(t)
                
        return json.loads(t)