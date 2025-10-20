from unittest.mock import MagicMock, patch

import pytest
from repository.task_repository import TaskRepository


@pytest.fixture
def mock_connection():
    conn = MagicMock()
    cur = MagicMock()
    conn.cursor.return_value = cur
    return conn, cur


@patch("repository.task_repository.pymysql.connect")
def test_get_all(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 1, "title": "Task 1"}]
    mock_connect.return_value = conn

    repo = TaskRepository()
    result = repo.get_all(team_id=1)

    assert result == [{"id": 1, "title": "Task 1"}]
    cur.execute.assert_called_once()


@patch("repository.task_repository.pymysql.connect")
def test_insert(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.lastrowid = 55
    mock_connect.return_value = conn

    repo = TaskRepository()
    task_id = repo.insert(
        title="New Task",
        description="Desc",
        due_date="2025-12-31",
        responsible_id=1,
        team_id=1,
        status_id=1,
        subtasks=[],
    )

    assert task_id == 55
    conn.commit.assert_called_once()
    cur.execute.assert_called()


@patch("repository.task_repository.pymysql.connect")
def test_modify(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TaskRepository()
    rowcount = repo.modify(task_id=1, title="Updated Task")

    assert rowcount == 1
    conn.commit.assert_called_once()


@patch("repository.task_repository.pymysql.connect")
def test_delete(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TaskRepository()
    rowcount = repo.delete(task_id=1)

    assert rowcount == 1
    conn.commit.assert_called_once()
