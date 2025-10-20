from unittest.mock import MagicMock, patch

import pytest
from repository.subtask_repository import SubtaskRepository


@pytest.fixture
def mock_connection():
    conn = MagicMock()
    cur = MagicMock()
    conn.cursor.return_value = cur
    return conn, cur


@patch("repository.subtask_repository.pymysql.connect")
def test_get_all(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 1, "title": "Subtask 1", "done": False}]
    mock_connect.return_value = conn

    repo = SubtaskRepository()
    result = repo.get_all(task_id=1)

    assert result == [{"id": 1, "title": "Subtask 1", "done": False}]
    cur.execute.assert_called_once()


@patch("repository.subtask_repository.pymysql.connect")
def test_insert(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.lastrowid = 42
    mock_connect.return_value = conn

    repo = SubtaskRepository()
    subtask_id = repo.insert(task_id=1, title="New subtask", done=False)

    assert subtask_id == 42
    cur.execute.assert_called_once()
    conn.commit.assert_called_once()


@patch("repository.subtask_repository.pymysql.connect")
def test_modify(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = SubtaskRepository()
    rowcount = repo.modify(subtask_id=1, title="Updated", done=True)

    assert rowcount == 1
    conn.commit.assert_called_once()


@patch("repository.subtask_repository.pymysql.connect")
def test_delete(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = SubtaskRepository()
    rowcount = repo.delete(subtask_id=1)

    assert rowcount == 1
    conn.commit.assert_called_once()
