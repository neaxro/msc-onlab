from flask import current_app, jsonify
from flask_restful import Resource
from repository.keycloak_repository import KeycloakRepository
from service.auth_service import AuthService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import requires_auth


class UserFinder(Resource):
    def __init__(self):
        self.auth_service = AuthService(KeycloakRepository())

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, username):
        try:
            current_app.logger.info(
                f"Attempt for fetching user's data with username {username}"
            )

            try:
                user_data = self.auth_service.get_user_by_name(username)
                current_app.logger.info(
                    f"Successful fetched user data with username {username}"
                )
                return jsonify(user_data)
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during fetching user data username is {username}"
                )
                return {
                    "error": f"Error occured during fetching user data username is {username}",  # noqa: E501
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
