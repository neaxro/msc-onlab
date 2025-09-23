from repository.keycloak_repository import KeycloakRepository


class AuthService:
    def __init__(self, keycloak_repository: KeycloakRepository):
        self.keycloak_repository = keycloak_repository

    def login(self, data):
        username = data.get("username")
        password = data.get("password")

        if not username:
            raise Exception("Username is required!")

        if not password:
            raise Exception("Password is required!")

        return self.keycloak_repository.login(username, password)

    def register(self, data):
        first_name = data.get("first_name")
        last_name = data.get("last_name")
        email = data.get("email")
        username = data.get("username")
        password = data.get("password")
        profile_picture = data.get("profile_picture")

        if not all([first_name, last_name, email, username, password]):
            raise Exception(f'Missing fields for "{username}" user.')

        return self.keycloak_repository.register(
            first_name=first_name,
            last_name=last_name,
            email=email,
            username=username,
            password=password,
            profile_picture=profile_picture if profile_picture else "default",
        )

    def get_user_by_id(self, user_id, metadata=False):
        if not user_id:
            raise Exception("User's id is required!")

        return self.keycloak_repository.get_user_by_id(user_id, metadata)

    def get_user_by_name(self, username):
        if not username:
            raise Exception("User's username is required!")

        return self.keycloak_repository.get_user_by_username(username)

    def modify_user(self, user_id, data):
        if not user_id:
            raise Exception("User's id is required!")

        user_data = self.get_user_by_id(user_id)
        if user_data["username"] != data["username"]:
            raise Exception("You cannot modify other users' data!")

        first_name = data.get("first_name")
        last_name = data.get("last_name")
        email = data.get("email")
        username = data.get("username")
        password = data.get("password")
        profile_picture = data.get("profile_picture")

        if not all([first_name, last_name, email, password, profile_picture]):
            raise Exception(f'Missing fields for "{username}" user.')

        return self.keycloak_repository.modify_user(
            user_id=user_id,
            first_name=first_name,
            last_name=last_name,
            email=email,
            password=password,
            profile_picture=profile_picture,
        )

    def enable_user(self, user_id):
        if not user_id:
            raise Exception("User's id is required!")

        return self.keycloak_repository.enable_user(user_id)

    def disable_user(self, user_id):
        if not user_id:
            raise Exception("User's id is required!")

        return self.keycloak_repository.disable_user(user_id)
