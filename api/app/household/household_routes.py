from flask_restful import Resource, request
from app import api, app

from app.household.household_service import HouseholdService

class HouseholdResource(Resource):
    def __init__(self):
        self.household_service = HouseholdService()
    
    def get(self, detailed='brief', id=None):
        try:
            if id is not None:
                household = self.household_service.get_by_id(id)
                return household
            
            elif detailed == 'detailed':
                households = self.household_service.get_all()
                return households
            
            else:
                households = self.household_service.get_all_brief()
                return households
        except Exception as e:
            return app.response_class(
                    response=str(e),
                    status=500,
                    mimetype='application/json'
                )
    
    def post(self):
        request_type = request.headers.get('Content-Type')
        if request_type == 'application/json':
            body = request.get_json()
            
            try:
                self.household_service.validate_json_format_insert(body)
                
                result = self.household_service.insert_household(body)
                
                if result.acknowledged:
                    return app.response_class(
                        response=str(result.inserted_id),
                        status=200,
                        mimetype='application/json'
                    )
                
                else:
                    return app.response_class(
                        response="Error",
                        status=404,
                        mimetype='application/json'
                    )
            
            except Exception as e:
                return app.response_class(
                    response=str(e),
                    status=500,
                    mimetype='application/json'
                )
    
    def patch(self, id):        
        request_type = request.headers.get('Content-Type')
        if request_type == 'application/json':
            body = request.get_json()
            
            try:
                self.household_service.validate_json_format_modify(body)
                
                result = self.household_service.replace_household(id, body)
                
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
    def delete(self, id):
        try:            
            result = self.household_service.delete_household(id)
            
            if result.deleted_count > 0:
                return app.response_class(
                    response="Deleted",
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