from flask_restful import Resource
from app import api
import json

from app.auth.login.login_service import LoginService

class LoginResource(Resource):
    def __init__(self):
        self.login_service = LoginService()
    
    def get(self):
        data = self.login_service.get_data()
        return data
