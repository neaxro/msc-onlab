from flask import current_app, jsonify, make_response, request
from flask_restful import Resource
from repository.keycloak_repository import KeycloakRepository
from service.auth_service import AuthService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import requires_auth


class User(Resource):
    def __init__(self):
        self.auth_service = AuthService(KeycloakRepository())

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, user_id):
        try:
            current_app.logger.info(
                f"Attempt for fetching user's data with user id {user_id}"
            )

            try:
                user_data = self.auth_service.get_user_by_id(user_id)
                current_app.logger.info(
                    f"Successful fetched user data with id {user_id}"
                )
                return jsonify(user_data)
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during fetching user data user_id is {user_id}"
                )
                return {
                    "error": f"Error occured during fetching user data user_id is {user_id}",  # noqa: E501
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def patch(self, user_id):
        try:
            data = request.get_json()
            current_app.logger.info(f"Modifying user's data with user id {user_id}")

            try:
                user_data = self.auth_service.modify_user(user_id, data)
                current_app.logger.info(
                    f"Successful modified user data with id {user_id}"
                )
                return make_response(jsonify(user_data), 200)
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during modifying user data user_id is {user_id}"
                )
                return {
                    "error": f"Error occured during modifying user data user_id is {user_id}",  # noqa: E501
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def delete(self, user_id):
        try:
            current_app.logger.info(f"Disableing user with user id {user_id}")

            try:
                user_data = self.auth_service.disable_user(user_id)
                current_app.logger.info(f"Successful disabled user with id {user_id}")
                return make_response(jsonify(user_data), 204)

            except Exception as e:
                current_app.logger.info(
                    f"Error occured during disableing user which user_id is {user_id}"
                )
                return {
                    "error": f"Error occured during disableing user which user_id is {user_id}",  # noqa: E501
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
