import os
from app import app, api
from app.create_env import create_env_variables

from app.auth.login.login_routes import LoginResource
from app.auth.register.register_routes import RegisterResource

from app.user.user_routes import UserResource

from app.household.household_routes import HouseholdResource
from app.household.household_detailed_routes import HouseholdDetailedResource
from app.household.invitation.invitation_create_routes import InvitationCreateResource
from app.household.invitation.invitation_accept_routes import InvitationAcceptResource

from app.task.task_routes import TaskResource
from app.task.task_detailed_routes import TaskDetailedResource

# Auth resources
api.add_resource(LoginResource, '/auth/login')
api.add_resource(RegisterResource, '/auth/register')

# User resources
api.add_resource(UserResource, '/user', '/user/<id>')

# Hosuehold resources
api.add_resource(HouseholdResource, '/household', '/household/all', '/household/id/<household_id>')
api.add_resource(HouseholdDetailedResource, '/household', '/household/all/detailed', '/household/id/<household_id>')
api.add_resource(InvitationCreateResource, '/household/invite/')
api.add_resource(InvitationAcceptResource, '/household/accept-invite/<invitation_token>')


# Task resources
api.add_resource(TaskResource, '/task', '/task/all/household/<household_id>', '/task/add-to/<household_id>')
api.add_resource(TaskDetailedResource, '/task', '/task/all/household/<household_id>/detailed')

if __name__ == '__main__':
    create_env_variables()
    app.run(debug=True)