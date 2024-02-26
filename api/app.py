import os
from app import app, api
from app.create_env import create_env_variables
from app.auth.login.login_routes import LoginResource

api.add_resource(LoginResource, '/auth/login')

if __name__ == '__main__':
    create_env_variables()
    app.run(debug=True)