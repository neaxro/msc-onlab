from flask_restful import Resource, request, reqparse
from app import api, app
import json

from app.user.user_service import UserService

class UserResource(Resource):
    def __init__(self):
        self.user_service = UserService()
    
    def get(self, id=None):
        try:
            if id is not None:
                result = self.user_service.get_user_by_id(id)
                return result
        
            else:
                return app.response_class(
                                response="No id provided!",
                                status=404,
                                mimetype='application/json'
                            )
            
        except Exception as e:
            return app.response_class(
                    response=str(e),
                    status=500,
                    mimetype='application/json'
                )
    
    def patch(self):
        request_type = request.headers.get('Content-Type')
        if request_type == 'application/json':
            body = request.get_json()
            
            try:
                self.user_service.validate_json_format(body)
                
                result = self.user_service.replace_user(body)
                
                if result.matched_count > 0:
                    return app.response_class(
                        response="Updated",
                        status=200,
                        mimetype='application/json'
                    )
                
                else:
                    return app.response_class(
                        response="No mach",
                        status=404,
                        mimetype='application/json'
                    )
            
            except Exception as e:
                return app.response_class(
                    response=str(e),
                    status=500,
                    mimetype='application/json'
                )