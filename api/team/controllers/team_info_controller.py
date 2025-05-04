from flask import current_app, jsonify
from flask_restful import Resource
from repository.auth_service_repository import AuthServiceRepository
from repository.team_repository import TeamRepository
from repository.team_user_repository import TeamUserRepository
from service.team_service import TeamService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import requires_auth


class TeamInfoController(Resource):
    def __init__(self):
        self.team_service = TeamService(
            team_repositry=TeamRepository(),
            team_user_repository=TeamUserRepository(),
            auth_service_repository=AuthServiceRepository(),
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, team_id):
        try:
            team_info = self.team_service.team_info(team_id)

            return jsonify(team_info)

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
