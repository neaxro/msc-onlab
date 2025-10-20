from unittest.mock import MagicMock

import pytest
from repository.auth_service_repository import AuthServiceRepository
from repository.invitation_repository import InvitationRepository
from repository.team_repository import TeamRepository
from service.invitation_service import InvitationService

# ---------------------------
# Fixtures
# ---------------------------


@pytest.fixture
def mock_invitation_repo():
    return MagicMock(spec=InvitationRepository)


@pytest.fixture
def mock_auth_repo():
    return MagicMock(spec=AuthServiceRepository)


@pytest.fixture
def mock_team_repo():
    return MagicMock(spec=TeamRepository)


@pytest.fixture
def invitation_service(mock_invitation_repo, mock_auth_repo, mock_team_repo):
    return InvitationService(
        invitation_repository=mock_invitation_repo,
        auth_repository=mock_auth_repo,
        team_repository=mock_team_repo,
    )


# ---------------------------
# Tests
# ---------------------------


def test_get_pending_invitations(
    invitation_service, mock_invitation_repo, mock_auth_repo, mock_team_repo
):
    mock_invitation_repo.get_pending_for_user.return_value = [
        {"inviter_user_id": "user1", "team_id": 1, "token": "tok", "email": "a@b.com"}
    ]
    mock_auth_repo.get_user_by_id.return_value = {"id": "user1", "name": "Inviter"}
    mock_team_repo.get_teams_info.return_value = {"id": 1, "name": "Team1"}

    result = invitation_service.get_pending_invitations("user2", "auth-header")

    assert result[0]["inviter"]["name"] == "Inviter"
    assert result[0]["team"]["name"] == "Team1"
    assert "inviter_user_id" not in result[0]
    assert "team_id" not in result[0]


def test_insert_invitation(
    invitation_service, mock_auth_repo, mock_team_repo, mock_invitation_repo
):
    mock_auth_repo.get_user_by_id.return_value = {
        "id": "user2",
        "email": "test@example.com",
    }
    mock_team_repo.get_all_members.return_value = [{"id": "user1"}]
    mock_invitation_repo.insert.return_value = 1

    data = {"team_id": 1, "invited_user_id": "user2"}
    result = invitation_service.insert(
        data, inviter_user_id="user1", auth_header="auth-header"
    )

    assert result == 1
    mock_invitation_repo.insert.assert_called_once()
    called_args = mock_invitation_repo.insert.call_args[1]
    assert called_args["invited_user_id"] == "user2"
    assert called_args["inviter_user_id"] == "user1"
    assert called_args["team_id"] == 1
    assert called_args["email"] == "test@example.com"
    assert isinstance(called_args["expires"], str)
    assert isinstance(called_args["token"], str)


def test_respond_to_invitation_accept(
    invitation_service, mock_invitation_repo, mock_team_repo
):
    token = "tok"
    mock_invitation_repo.get_invitation_by_token.return_value = {
        "invited_user_id": "user2",
        "team_id": 1,
    }
    mock_invitation_repo.update_invitation_status.return_value = 1

    invitation_service.respond_to_invitation("user2", token, "accept", "auth-header")

    mock_team_repo.add_user_to_team.assert_called_once_with(
        user_id="user2", team_id=1, auth_header="auth-header"
    )
    mock_invitation_repo.update_invitation_status.assert_called_once_with(
        token, accepted=True
    )


def test_respond_to_invitation_reject(
    invitation_service, mock_invitation_repo, mock_team_repo
):
    token = "tok"
    mock_invitation_repo.get_invitation_by_token.return_value = {
        "invited_user_id": "user2",
        "team_id": 1,
    }
    mock_invitation_repo.update_invitation_status.return_value = 1

    invitation_service.respond_to_invitation("user2", token, "reject", "auth-header")

    mock_team_repo.add_user_to_team.assert_not_called()
    mock_invitation_repo.update_invitation_status.assert_called_once_with(
        token, accepted=False
    )
