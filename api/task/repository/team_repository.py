import requests
from utils.config import Config


class TeamRepository:
    def __init__(self):
        self.config = Config()

    def get_teams_info(self, team_id, auth_header):
        try:
            team_service_host = self.config.TEAM_SERVICE_HOST
            team_service_port = self.config.TEAM_SERVICE_PORT

            headers = {"Authorization": auth_header}
            url = f"http://{team_service_host}:{team_service_port}/teams/{team_id}/info"

            response = requests.get(url=url, headers=headers)

            data = response.json()

            return data

        except Exception:
            raise Exception(
                f"Failed to call Team Service's team info endpoint: http://{team_service_host}:{team_service_port}/teams/{team_id}/info"  # noqa: E501
            )
