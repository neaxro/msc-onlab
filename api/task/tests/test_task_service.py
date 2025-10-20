from unittest.mock import MagicMock

import pytest
from service.task_service import TaskService


@pytest.fixture
def mock_repositories():
    task_repo = MagicMock()
    subtask_repo = MagicMock()
    team_repo = MagicMock()
    auth_repo = MagicMock()
    return task_repo, subtask_repo, team_repo, auth_repo


def test_get_all_tasks(mock_repositories):
    task_repo, subtask_repo, team_repo, auth_repo = mock_repositories
    task_repo.get_all.return_value = [
        {"id": 1, "title": "Task1", "responsible_id": "user1"}
    ]
    subtask_repo.get_all.return_value = [{"id": 11, "title": "Subtask1", "done": 0}]
    auth_repo.get_user_by_id.return_value = {"id": "user1", "name": "John"}

    service = TaskService(task_repo, subtask_repo, team_repo, auth_repo)
    result = service.get_all(team_id=5, auth_header="Bearer token")

    assert result[0]["responsible"]["name"] == "John"
    assert result[0]["subtasks"][0]["title"] == "Subtask1"


def test_insert_task_success(mock_repositories):
    task_repo, subtask_repo, team_repo, auth_repo = mock_repositories
    team_repo.get_teams_info.return_value = {
        "id": 5,
        "statuses": [{"id": 1, "name": "TODO"}],
    }
    team_repo.get_all_members.return_value = [{"id": "user1"}]
    task_repo.insert.return_value = 99

    service = TaskService(task_repo, subtask_repo, team_repo, auth_repo)
    data = {
        "title": "New Task",
        "description": "Description",
        "due_date": "2025-10-21",
        "team_id": 5,
        "responsible_id": "user1",
        "subtasks": [{"title": "Subtask", "done": False}],
    }

    result = service.insert(data, auth_header="Bearer token")

    assert result == 99
    task_repo.insert.assert_called_once()


def test_insert_task_missing_field(mock_repositories):
    task_repo, subtask_repo, team_repo, auth_repo = mock_repositories
    service = TaskService(task_repo, subtask_repo, team_repo, auth_repo)
    data = {"description": "Description", "due_date": "2025-10-21", "team_id": 5}

    with pytest.raises(Exception, match="Missing required field: title"):
        service.insert(data, auth_header="Bearer token")


def test_modify_task_success(mock_repositories):
    task_repo, subtask_repo, team_repo, auth_repo = mock_repositories
    task_repo.modify.return_value = 1
    service = TaskService(task_repo, subtask_repo, team_repo, auth_repo)

    result = service.modify({"id": 5, "title": "Updated"})
    assert result == 1
    task_repo.modify.assert_called_once_with(
        task_id=5,
        title="Updated",
        description=None,
        due_date=None,
        status_id=None,
        responsible_id=None,
    )


def test_modify_task_missing_id(mock_repositories):
    task_repo, subtask_repo, team_repo, auth_repo = mock_repositories
    service = TaskService(task_repo, subtask_repo, team_repo, auth_repo)

    with pytest.raises(Exception, match="Task's id must be provided!"):
        service.modify({"title": "Updated"})
