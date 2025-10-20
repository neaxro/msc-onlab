from unittest.mock import MagicMock, patch

from repository.team_repository import TeamRepository


@patch("repository.team_repository.requests.get")
def test_get_teams_info(mock_get):
    mock_response = MagicMock()
    mock_response.json.return_value = {"id": 1, "name": "DevOps"}
    mock_get.return_value = mock_response

    repo = TeamRepository()
    data = repo.get_teams_info(team_id=1, auth_header="Bearer token")

    assert data == {"id": 1, "name": "DevOps"}
    mock_get.assert_called_once()


@patch("repository.team_repository.requests.get")
def test_get_all_members(mock_get):
    mock_response = MagicMock()
    mock_response.json.return_value = [{"id": "user1"}, {"id": "user2"}]
    mock_get.return_value = mock_response

    repo = TeamRepository()
    members = repo.get_all_members(team_id=1, auth_header="Bearer token")

    assert members == [{"id": "user1"}, {"id": "user2"}]
    mock_get.assert_called_once()
