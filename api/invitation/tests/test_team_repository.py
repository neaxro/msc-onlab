from unittest.mock import patch

from repository.team_repository import TeamRepository


@patch("repository.team_repository.requests.get")
def test_team_repository_get_teams_info(mock_get):
    mock_get.return_value.json.return_value = {"id": 1, "name": "Team1"}
    repo = TeamRepository()
    result = repo.get_teams_info(1, "auth-header")
    assert result["name"] == "Team1"


@patch("repository.team_repository.requests.get")
def test_team_repository_get_all_members(mock_get):
    mock_get.return_value.json.return_value = [{"id": "user1"}]
    repo = TeamRepository()
    result = repo.get_all_members(1, "auth-header")
    assert result[0]["id"] == "user1"


@patch("repository.team_repository.requests.post")
def test_team_repository_add_user_to_team(mock_post):
    mock_post.return_value.json.return_value = {"success": True}
    repo = TeamRepository()
    result = repo.add_user_to_team("user2", 1, "auth-header")
    assert result["success"] is True
