import secrets
from datetime import datetime, timedelta

from repository.auth_service_repository import AuthServiceRepository
from repository.invitation_repository import InvitationRepository
from repository.team_repository import TeamRepository
from utils.config import Config


class InvitationService:
    """Handles business logic for invitations."""

    def __init__(
        self,
        invitation_repository: InvitationRepository,
        auth_repository: AuthServiceRepository,
        team_repository: TeamRepository,
    ):
        config = Config()

        self.invitation_repository = invitation_repository
        self.auth_repository = auth_repository
        self.team_repository = team_repository
        self.expiration_days = config.EXPIRATION_DAYS

    def _get_invited_user(self, user_id, auth_header):
        return self.auth_repository.get_user_by_id(user_id, auth_header)

    def _get_team(self, team_id, auth_header):
        return self.team_repository.get_teams_info(team_id, auth_header)

    def insert(self, data, inviter_user_id, auth_header):
        # Validate required fields
        required_fields = ["team_id", "invited_user_id"]
        for field in required_fields:
            if field not in data or data[field] is None:
                raise Exception(f"Missing required field: {field}")

        invited_user_id = data["invited_user_id"]
        invited_user = self._get_invited_user(invited_user_id, auth_header)

        team_id = data["team_id"]

        token = secrets.token_urlsafe(32)
        email = invited_user["email"]
        expires = (datetime.now() + timedelta(days=self.expiration_days)).strftime(
            "%Y-%m-%d %H:%M:%S"
        )

        return self.invitation_repository.insert(
            invited_user_id=invited_user_id,
            inviter_user_id=inviter_user_id,
            team_id=team_id,
            token=token,
            email=email,
            expires=expires,
        )
