from flask import current_app, request
from flask_restful import Resource
from utils.config import Config
from utils.metrics import count_requests, latency_request, time_request

from keycloak import KeycloakAdmin


class Register(Resource):
    def __init__(self):
        config = Config()

        self.keycloak_admin = KeycloakAdmin(
            server_url=config.KEYCLOAK_SERVER_URL,
            username=config.KEYCLOAK_ADMIN_USERNAME,
            password=config.KEYCLOAK_ADMIN_PASSWORD,
            realm_name=config.KEYCLOAK_REALM_NAME,
            client_id=config.KEYCLOAK_CLIENT_ID,
            client_secret_key=config.KEYCLOAK_CLIENT_SECRET,
            verify=True,
        )

    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            data = request.get_json()

            first_name = data.get("first_name")
            last_name = data.get("last_name")
            email = data.get("email")
            username = data.get("username")
            password = data.get("password")

            current_app.logger.info(f'Registration attempt for "{username}" user.')

            if not all([first_name, last_name, email, username, password]):
                current_app.logger.info(f'Missing fields for "{username}" user.')
                return {"error": "Missing field(s)"}, 400

            try:
                user_id = self.keycloak_admin.create_user(
                    {
                        "username": username,
                        "email": email,
                        "firstName": first_name,
                        "lastName": last_name,
                        "enabled": True,
                        "emailVerified": True,
                        "credentials": [
                            {"type": "password", "value": password, "temporary": False}
                        ],
                    }
                )

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
