import os
from app import app, api
from app.create_env import create_env_variables

from app.auth.login.login_routes import LoginResource
from app.auth.register.register_routes import RegisterResource
from app.user.user_routes import UserResource
from app.household.household_routes import HouseholdResource

from app.task.task_routes import TaskResource
from app.task.task_detailed_routes import TaskDetailedResource

api.add_resource(LoginResource, '/auth/login')
api.add_resource(RegisterResource, '/auth/register')
api.add_resource(UserResource, '/user', '/user/<id>')
api.add_resource(HouseholdResource, '/household', '/household/<detailed>', '/household/id/<id>')
# Task resources
api.add_resource(TaskResource, '/task', '/task/all/household/<household_id>', '/task/add-to/<household_id>')
api.add_resource(TaskDetailedResource, '/task', '/task/all/household/<household_id>/detailed')

if __name__ == '__main__':
    create_env_variables()
    app.run(debug=True)