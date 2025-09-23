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

    def register(
        self,
        first_name,
        last_name,
        email,
        username,
        password,
        profile_picture="default",
    ):
        user_data = {
            "username": username,
            "email": email,
            "firstName": first_name,
            "lastName": last_name,
            "enabled": True,
            "emailVerified": True,
            "attributes": {"locale": [""], "profilePicture": [profile_picture]},
            "credentials": [
                {"type": "password", "value": password, "temporary": False}
            ],
        }
        user_id = self.keycloak_admin.create_user(user_data)

        return user_id

    def get_user_by_id(self, user_id, metadata=False):
        user = self.keycloak_admin.get_user(user_id, metadata)
        user.pop("access")

        return user

    def get_user_by_username(self, username):
        users = self.keycloak_admin.get_users({"username": username})

        if not users:
            return None

        user = users[0]
        user.pop("access", None)

        return user

    def modify_user(
        self, user_id, first_name, last_name, email, password, profile_picture
    ):
        user_data = {
            "email": email,
            "firstName": first_name,
            "lastName": last_name,
            "enabled": True,
            "emailVerified": True,
            "attributes": {"locale": [""], "profilePicture": [profile_picture]},
            "credentials": [
                {"type": "password", "value": password, "temporary": False}
            ],
        }
        response = self.keycloak_admin.update_user(user_id, user_data)

        return response

    def enable_user(self, user_id):
        response = self.keycloak_admin.enable_user(user_id)
        return response

    def disable_user(self, user_id):
        response = self.keycloak_admin.disable_user(user_id)
        return response
