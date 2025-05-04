import requests
from utils.config import Config


class AuthServiceRepository:
    def __init__(self):
        self.config = Config()

    def get_user_by_id(self, user_id, auth_header):
        try:
            auth_service_host = self.config.AUTH_SERVICE_HOST
            auth_service_port = self.config.AUTH_SERVICE_PORT

            headers = {"Authorization": auth_header}
            url = f"http://{auth_service_host}:{auth_service_port}/user/{user_id}"

            response = requests.get(url=url, headers=headers)

            data = response.json()

            if "error" in data:
                raise Exception(f"Error during fetching user with id {user_id}")

            return data

        except Exception:
            raise Exception(
                f"Failed to call Auth Service's user info endpoint: http://{auth_service_host}:{auth_service_port}/user/{user_id}"  # noqa: E501
            )
