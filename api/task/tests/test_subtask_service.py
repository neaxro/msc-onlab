from unittest.mock import MagicMock

import pytest
from service.subtask_service import SubtaskService


@pytest.fixture
def mock_repositories():
    subtask_repo = MagicMock()
    task_repo = MagicMock()
    auth_repo = MagicMock()
    return subtask_repo, task_repo, auth_repo


def test_get_all_subtasks(mock_repositories):
    subtask_repo, task_repo, auth_repo = mock_repositories
    subtask_repo.get_all.return_value = [{"id": 1, "title": "Subtask1", "done": False}]

    service = SubtaskService(subtask_repo, task_repo, auth_repo)
    result = service.get_all(10)

    assert result == [{"id": 1, "title": "Subtask1", "done": False}]
    subtask_repo.get_all.assert_called_once_with(10)


def test_insert_subtask_success(mock_repositories):
    subtask_repo, task_repo, auth_repo = mock_repositories
    task_repo.get_by_id.return_value = {"id": 5, "title": "Task 1"}
    subtask_repo.insert.return_value = 99

    service = SubtaskService(subtask_repo, task_repo, auth_repo)
    result = service.insert({"task_id": 5, "title": "New Subtask", "done": False})

    assert result == 99
    subtask_repo.insert.assert_called_once_with(
        task_id=5, title="New Subtask", done=False
    )


def test_insert_subtask_missing_field(mock_repositories):
    subtask_repo, task_repo, auth_repo = mock_repositories
    service = SubtaskService(subtask_repo, task_repo, auth_repo)

    with pytest.raises(Exception, match="Missing required field: title"):
        service.insert({"task_id": 5, "done": False})


def test_modify_subtask_success(mock_repositories):
    subtask_repo, task_repo, auth_repo = mock_repositories
    subtask_repo.modify.return_value = 1

    service = SubtaskService(subtask_repo, task_repo, auth_repo)
    result = service.modify({"id": 10, "title": "Updated", "done": True})

    assert result == 1
    subtask_repo.modify.assert_called_once_with(
        subtask_id=10, title="Updated", done=True
    )


def test_modify_subtask_missing_id(mock_repositories):
    subtask_repo, task_repo, auth_repo = mock_repositories
    service = SubtaskService(subtask_repo, task_repo, auth_repo)

    with pytest.raises(Exception, match="Subtask's id must be provided!"):
        service.modify({"title": "Updated"})
