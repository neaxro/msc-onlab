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

    def get_pending_invitations(self, user_id, auth_header):
        invitations = self.invitation_repository.get_pending_for_user(user_id)

        for invitation in invitations:
            invitation["inviter"] = self._get_user_data(
                invitation["inviter_user_id"], auth_header
            )
            invitation.pop("inviter_user_id")

            invitation["team"] = self._get_team_data(invitation["team_id"], auth_header)
            invitation.pop("team_id")

        return invitations

    def _get_user_data(self, user_id, auth_header):
        return self.auth_repository.get_user_by_id(user_id, auth_header)

    def _get_team_data(self, team_id, auth_header):
        return self.team_repository.get_teams_info(team_id, auth_header)

    def insert(self, data, inviter_user_id, auth_header):
        # Validate required fields
        required_fields = ["team_id", "invited_user_id"]
        for field in required_fields:
            if field not in data or data[field] is None:
                raise Exception(f"Missing required field: {field}")

        invited_user_id = data["invited_user_id"]
        invited_user = self._get_user_data(invited_user_id, auth_header)

        team_id = data["team_id"]
        team_users = self.team_repository.get_all_members(team_id, auth_header)

        if any(user["id"] == invited_user_id for user in team_users):
            raise Exception("Invited user is already present in the team!")

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

    def respond_to_invitation(self, user_id, token, decision, auth_header):
        invitation = self.invitation_repository.get_invitation_by_token(token)
        if not invitation:
            raise Exception("Invalid or expired invitation token.")

        if invitation["invited_user_id"] != user_id:
            raise Exception("User is not invited by this invitation!")

        if decision == "accept":
            self.team_repository.add_user_to_team(
                user_id=invitation["invited_user_id"],
                team_id=invitation["team_id"],
                auth_header=auth_header,
            )

        updated_rows = self.invitation_repository.update_invitation_status(
            token, accepted=(decision == "accept")
        )

        if updated_rows == 0:
            raise Exception("Invitation could not be updated.")
