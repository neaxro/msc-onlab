from utils.config import Config

from keycloak import KeycloakAdmin, KeycloakOpenID


class KeycloakRepository:
    def __init__(self):
        self.config = Config()

        self.keycloak_admin = KeycloakAdmin(
            server_url=self.config.KEYCLOAK_SERVER_URL,
            username=self.config.KEYCLOAK_ADMIN_USERNAME,
            password=self.config.KEYCLOAK_ADMIN_PASSWORD,
            realm_name=self.config.KEYCLOAK_REALM_NAME,
            client_id=self.config.KEYCLOAK_CLIENT_ID,
            client_secret_key=self.config.KEYCLOAK_CLIENT_SECRET,
            verify=True,
        )

        self.keycloak_openid = KeycloakOpenID(
            server_url=self.config.KEYCLOAK_SERVER_URL,
            realm_name=self.config.KEYCLOAK_REALM_NAME,
            client_id=self.config.KEYCLOAK_CLIENT_ID,
            client_secret_key=self.config.KEYCLOAK_CLIENT_SECRET,
        )

    def login(self, username, password):
        token = self.keycloak_openid.token(username, password)
        return token

    def register(self, first_name, last_name, email, username, password):
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

        return user_id
