from flask_restful import Resource, request
from app import api, app
import json

from app.auth.login.login_service import LoginService
from app.decorators.token_requires import *

class LoginResource(Resource):
    def __init__(self):
        self.login_service = LoginService()
    
    # Example for token data
    @token_required
    def get(self, user_data):
        return user_data
    
    def post(self):
        request_type = request.headers.get('Content-Type')
        if request_type == 'application/json':
            body = request.get_json()
            
            try:
                self.login_service.validate_json_format(body)
                
                token = self.login_service.login_user(body)
                
                result = { 'token': str(token) }
                
                return app.response_class(
                    response=json.dumps(result),
                    status=200,
                    mimetype='application/json'
                )
            
            except Exception as e:
                return app.response_class(
                    response=str(e),
                    status=500,
                    mimetype='application/json'
                )
            
            
