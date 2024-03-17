from flask_restful import Resource, request
from app import api, app

from app.household.tasks.task_service import TaskService
from app.utils.templater import Templater
from app.decorators.token_requires import token_required

class TaskDetailedResource(Resource):
    def __init__(self):
        self.task_service = TaskService()
        self.templater = Templater()
    
    def get(self, household_id=None):
        try:
            tasks = self.task_service.get_all(household_id)
            
            return app.response_class(
                response=self.templater.get_basic_succes_template(
                    status="Success",
                    data=tasks
                ),
                status=200,
                mimetype='application/json'
            )
        
        except Exception as e:
                return app.response_class(
                    response=self.templater.get_basic_error_template(
                        error_message=str(e)
                    ),
                    status=500,
                    mimetype='application/json'
                )