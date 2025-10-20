from unittest.mock import MagicMock

import pytest
from service.team_service import TeamService


@pytest.fixture
def mock_repos():
    team_repo = MagicMock()
    team_user_repo = MagicMock()
    auth_repo = MagicMock()

    return team_repo, team_user_repo, auth_repo


@pytest.fixture
def team_service(mock_repos):
    team_repo, team_user_repo, auth_repo = mock_repos
    return TeamService(team_repo, team_user_repo, auth_repo)


def test_get_all_without_user(team_service, mock_repos):
    team_repo, _, _ = mock_repos
    team_repo.get_all.return_value = [{"id": 1, "name": "DevOps"}]

    result = team_service.get_all()
    assert result == [{"id": 1, "name": "DevOps"}]
    team_repo.get_all.assert_called_once_with()


def test_get_all_with_user(team_service, mock_repos):
    team_repo, _, _ = mock_repos
    user_data = {"sub": "123"}
    team_repo.get_all.return_value = [{"id": 1, "name": "Infra"}]

    result = team_service.get_all(user_data)
    assert result == [{"id": 1, "name": "Infra"}]
    team_repo.get_all.assert_called_once_with("123")


def test_insert_new_team(team_service, mock_repos):
    team_repo, team_user_repo, _ = mock_repos

    data = {"name": "Backend", "description": "API Team"}
    user_data = {"sub": "user-1"}

    team_repo.get_by_name.return_value = None
    team_repo.insert.return_value = 99
    team_user_repo.insert.return_value = 1

    new_id = team_service.insert(data, user_data)

    assert new_id == 99
    team_repo.insert.assert_called_once_with(data)
    team_user_repo.insert.assert_called_once_with(99, "user-1")


def test_insert_team_already_exists(team_service, mock_repos):
    team_repo, _, _ = mock_repos
    team_repo.get_by_name.return_value = {"id": 1, "name": "DevOps"}

    with pytest.raises(Exception) as excinfo:
        team_service.insert({"name": "DevOps"}, {"sub": "user-1"})
    assert "already exists" in str(excinfo.value)


def test_delete_not_part_of_team(team_service, mock_repos):
    _, team_user_repo, _ = mock_repos
    user_data = {"sub": "u1", "preferred_username": "testuser"}
    team_user_repo.is_user_part_of_team.return_value = None

    with pytest.raises(Exception) as excinfo:
        team_service.delete(1, user_data)
    assert "not part of team" in str(excinfo.value)
