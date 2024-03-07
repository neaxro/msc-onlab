from flask_restful import Resource, request
from app import api, app

from app.task.task_service import TaskService
from app.utils.templater import Templater
from app.decorators.token_requires import token_required

class TaskResource(Resource):
    def __init__(self):
        self.task_service = TaskService()
        self.templater = Templater()
    
    def get(self):
        return app.response_class(
            response=self.templater.get_basic_succes_template(
                status="Success",
                data="Tasks data..."
            ),
            status=200,
            mimetype='application/json'
        )