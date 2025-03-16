import time
from functools import wraps

from flask import current_app, jsonify, make_response, request
from utils.config import Config

from keycloak import KeycloakError, KeycloakOpenID


class KeycloakClient:
    def __new__(cls):
        if not hasattr(cls, "instance"):
            cls.instance = super(KeycloakClient, cls).__new__(cls)
        return cls.instance

    def __init__(self):
        config = Config()

        self.keycloak_openid = KeycloakOpenID(
            server_url=config.KEYCLOAK_SERVER_URL,
            realm_name=config.KEYCLOAK_REALM_NAME,
            client_id=config.KEYCLOAK_CLIENT_ID,
            client_secret_key=config.KEYCLOAK_CLIENT_SECRET,
        )


keycloakClient = KeycloakClient()


def requires_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        # Get Authorization header
        auth_header = request.headers.get("Authorization", None)
        if not auth_header or not auth_header.startswith("Bearer "):
            return make_response(
                jsonify({"message": "Unauthorized - No valid token"}), 401
            )

        token = auth_header.split(" ")[1]

        try:
            # Decode and validate token
            decoded_token = keycloakClient.keycloak_openid.decode_token(token)

            # Check if token is expired
            exp = decoded_token.get("exp", 0)
            current_time = int(time.time())

            if current_time >= exp:
                current_app.logger.debug("Token has expired!")
                return make_response(jsonify({"message": "Token has expired!"}), 401)

        except KeycloakError as e:
            print(f"Keycloak Error: {str(e)}")
            current_app.logger.info(
                f"Keycloak error during authentication check: {str(e)}"
            )
            return make_response(jsonify({"message": str(e)}), 401)
        except Exception as e:
            print(f"Unexpected Error: {str(e)}")
            current_app.logger.info(
                f"Unexpected error during authentication check: {str(e)}"
            )
            return make_response(jsonify({"message": str(e)}), 401)

        return f(*args, **kwargs)

    return decorated


def get_decoded_token_from_request():
    auth_header = request.headers.get("Authorization", None)
    if not auth_header or not auth_header.startswith("Bearer "):
        return None

    token = auth_header.split(" ")[1]
    return keycloakClient.keycloak_openid.decode_token(token)
