from flask import request, jsonify
from flask_restful import Resource
from keycloak import KeycloakOpenID


class Login(Resource):
    def __init__(self):
        self.keycloak_server_url = "http://localhost:8080"
        self.realm_name = "msc-onlab-test"
        self.client_id = "msc-onlab-auth-microservice-client-test"
        self.client_secret = "moo8oexa0Aitoon8chohCaeh8eith5ei"
        
        self.keycloak_openid = KeycloakOpenID(
            server_url=self.keycloak_server_url,
            realm_name=self.realm_name,
            client_id=self.client_id,
            client_secret_key=self.client_secret
        )

    def get(self):
        return "Login"
    
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
