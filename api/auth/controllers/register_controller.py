from flask import current_app, request
from flask_restful import Resource
from repository.keycloak_repository import KeycloakRepository
from service.auth_service import AuthService
from utils.metrics import count_requests, latency_request, time_request


class Register(Resource):
    def __init__(self):
        self.auth_service = AuthService(KeycloakRepository())

    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            data = request.get_json()
            username = data.get("username")

            current_app.logger.info(f'Registration attempt for "{username}" user.')

            try:
                user_id = self.auth_service.register(data)

                current_app.logger.info(
                    f'Successful registration for "{username}" user.'
                )

                return {"message": "User created successfully", "user_id": user_id}, 201
            except Exception as e:
                current_app.logger.info(
                    f'Failed to create "{username}" user. Error: {str(e)}'
                )
                return {"error": "Failed to create user", "details": str(e)}, 500

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
