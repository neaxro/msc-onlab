from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.keycloak_repository import KeycloakRepository
from service.auth_service import AuthService
from utils.metrics import count_requests, latency_request, time_request


class Login(Resource):
    def __init__(self):
        self.auth_service = AuthService(KeycloakRepository())

    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            data = request.get_json()
            username = data.get("username")

            current_app.logger.info(f'Login attempt for "{username}" user.')

            try:
                token = self.auth_service.login(data)
                current_app.logger.info(f'Successful login for "{username}" user.')
                return jsonify(token)
            except Exception as e:
                current_app.logger.info(
                    f'Invalid credentials or other error for "{username}" user.'
                )
                return {
                    "error": "Invalid credentials or other error",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
