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

        if not all([first_name, last_name, email, username, password]):
            raise Exception(f'Missing fields for "{username}" user.')

        return self.keycloak_repository.register(
            first_name=first_name,
            last_name=last_name,
            email=email,
            username=username,
            password=password,
        )
