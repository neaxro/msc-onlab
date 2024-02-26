from flask_restful import Resource
from app import api

class LoginResource(Resource):
    def get(self):
        return {'message': 'Login API endpoint'}
