from flask_restful import Resource, request
from app import api, app

from app.household.household_service import HouseholdService

class HouseholdResource(Resource):
    def __init__(self):
        self.household_service = HouseholdService()
    
    def get(self):
        households = self.household_service.get_all_brief()
        
        return households
