from flask import current_app, jsonify
from flask_restful import Resource
from repository.team_repository import TeamRepository
from repository.team_user_repository import TeamUserRepository
from service.team_service import TeamService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class MembershipController(Resource):
    def __init__(self):
        self.team_service = TeamService(
            team_repositry=TeamRepository(), team_user_repository=TeamUserRepository()
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def post(self, team_id, invited_user_id):
        try:
            user_data = get_decoded_token_from_request()
            current_app.logger.info(
                f"Assigning user {invited_user_id} to team {team_id}"
            )

            try:
                self.team_service.add_user(team_id, invited_user_id, user_data)
                current_app.logger.info(
                    f"User {invited_user_id} successfully assigned to team!"
                )
                return jsonify({"message": "User successfully assigned to team!"})
            except Exception as e:
                current_app.logger.info("Error occured during assigning user to team.")
                return {
                    "error": "Error occured during assigning user to team",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def delete(self, team_id, invited_user_id):
        try:
            user_data = get_decoded_token_from_request()
            current_app.logger.info(
                f"Removing user {invited_user_id} from team {team_id}"
            )

            try:
                self.team_service.remove_user(team_id, invited_user_id, user_data)
                current_app.logger.info(
                    f"User {invited_user_id} successfully removed from team!"
                )
                return jsonify({"message": "User successfully removed from team!"})
            except Exception as e:
                current_app.logger.info("Error occured during removing from team.")
                return {
                    "error": "Error occured during removing from team",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
