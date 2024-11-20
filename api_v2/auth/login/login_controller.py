from flask import request, jsonify
from flask_restful import Resource
from keycloak import KeycloakOpenID
from config import Config
from metrics import Metrics, count_requests, time_request, latency_request


class Login(Resource):
    def __init__(self):
        config = Config()
        
        self.keycloak_openid = KeycloakOpenID(
            server_url=config.KEYCLOAK_SERVER_URL,
            realm_name=config.KEYCLOAK_REALM_NAME,
            client_id=config.KEYCLOAK_CLIENT_ID,
            client_secret_key=config.KEYCLOAK_CLIENT_SECRET
        )
    
    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            data = request.get_json()
            username = data.get("username")
            password = data.get("password")

            if not username or not password:
                return {"error": "Username and password are required"}, 400

            # Authenticate user with Keycloak
            try:
                token = self.keycloak_openid.token(username, password)
                return jsonify(token)
            except Exception as e:
                return {"error": "Invalid credentials or other error", "details": str(e)}, 401

        except Exception as e:
            return {"error": "Something went wrong", "details": str(e)}, 500
