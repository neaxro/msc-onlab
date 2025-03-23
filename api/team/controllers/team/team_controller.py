from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.team_repository import TeamRepository
from repository.team_user_repository import TeamUserRepository
from service.team_service import TeamService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class TeamController(Resource):
    def __init__(self):
        self.team_service = TeamService(
            team_repositry=TeamRepository(), team_user_repository=TeamUserRepository()
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, team_id=None):
        try:
            user_data = get_decoded_token_from_request()
            current_app.logger.info(
                f"Getting all teams for user {user_data['preferred_username']}."
            )

            try:
                if team_id:
                    response = self.team_service.get_by_id(team_id, user_data)
                    current_app.logger.info(
                        f"Successfuly fetched team with {team_id} id for {user_data['preferred_username']}!"  # noqa: E501
                    )
                    return jsonify(response)

                else:
                    response = self.team_service.get_all(user_data)
                    current_app.logger.info("Successfuly fetched all team!")
                    return jsonify(response)

            except Exception as e:
                current_app.logger.info("Error occured during fetching all team.")
                return {
                    "error": "Error occured during fetching all team.",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            body = request.get_json()
            data = {"name": body.get("name"), "description": body.get("description")}
            user_data = get_decoded_token_from_request()

            current_app.logger.info(
                f"Creating new team with name \"{data['name']}\" initiated by {user_data['preferred_username']}"  # noqa: E501
            )

            try:
                response = self.team_service.insert(data, user_data)
                current_app.logger.info(
                    f"New team called \"{data['name']}\" successfuly created!"
                )
                return jsonify(
                    {"message": "New team successfuly created!", "team_id": response}
                )
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during \"{data['name']}\" team creation."
                )
                return {
                    "error": "Error occured during team creation",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def patch(self, team_id):
        try:
            body = request.get_json()
            data = {
                "id": team_id,
                "name": body.get("name"),
                "description": body.get("description"),
            }
            user_data = get_decoded_token_from_request()

            current_app.logger.info(
                f"Updating team with name \"{data['name']}\" initiated by {user_data['preferred_username']}"  # noqa: E501
            )

            try:
                self.team_service.update(data, user_data)
                current_app.logger.info(
                    f"New team called \"{data['name']}\" successfuly updated!"
                )
                return jsonify(
                    {"message": "Team successfuly updated!", "team_id": team_id}
                )
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during \"{data['name']}\" team updating..."
                )
                return {
                    "error": "Error occured during team updating...",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def delete(self, team_id):
        try:
            user_data = get_decoded_token_from_request()

            current_app.logger.info(
                f"Delete team with id \"{team_id}\" initiated by {user_data['preferred_username']}"  # noqa: E501
            )

            try:
                self.team_service.delete(team_id, user_data)
                current_app.logger.info(f"Team with id {team_id} successfuly deleted!")
                return jsonify(
                    {"message": "Team successfuly deleted!", "team_id": team_id}
                )
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during team with id {team_id} deletion..."
                )
                return {
                    "error": "Error occured during team deletion...",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
