from flask import request, jsonify
from flask_restful import Resource
from keycloak import KeycloakAdmin
from config import Config
from metrics import Metrics, count_requests, time_request, latency_request


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
            verify=True
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

            if not all([first_name, last_name, email, username, password]):
                return {"error": "Missing field(s)"}, 400

            try:
                user_id = self.keycloak_admin.create_user({
                    "username": username,
                    "email": email,
                    "firstName": first_name,
                    "lastName": last_name,
                    "enabled": True,
                    "emailVerified": True,
                    "credentials": [
                        {"type": "password", "value": password, "temporary": False}
                    ]
                })

                return {"message": "User created successfully", "user_id": user_id}, 201
            except Exception as e:
                return {"error": "Failed to create user", "details": str(e)}, 500

        except Exception as e:
            return {"error": "Something went wrong", "details": str(e)}, 500
