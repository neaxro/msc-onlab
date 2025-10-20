from unittest.mock import MagicMock, patch

import pytest
from repository.team_repository import TeamRepository


@pytest.fixture
def mock_connection():
    conn = MagicMock()
    cur = MagicMock()
    conn.cursor.return_value = cur
    return conn, cur


# --- get_all -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_get_all_without_user(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 1, "name": "DevOps"}]
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_all()

    assert result == [{"id": 1, "name": "DevOps"}]
    cur.execute.assert_called_once()
    conn.cursor.assert_called_once()


@patch("repository.team_repository.pymysql.connect")
def test_get_all_with_user(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 2, "name": "Backend"}]
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_all("user123")

    assert result == [{"id": 2, "name": "Backend"}]
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "WHERE tu.user_id" in args[0]


# --- get_by_name -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_get_by_name_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 3, "name": "QA"}]
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_by_name("QA")

    assert result == [{"id": 3, "name": "QA"}]
    cur.execute.assert_called_once_with("SELECT * FROM teams WHERE name = %s", ("QA",))


@patch("repository.team_repository.pymysql.connect")
def test_get_by_name_not_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = []
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_by_name("UnknownTeam")

    assert result is None


# --- get_by_id -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_get_by_id_without_user_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchone.return_value = {"id": 4, "name": "Infra"}
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_by_id(4)

    assert result == {"id": 4, "name": "Infra"}
    cur.execute.assert_called_once()


@patch("repository.team_repository.pymysql.connect")
def test_get_by_id_with_user_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchone.return_value = {"id": 5, "name": "Frontend"}
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_by_id(5, "user789")

    assert result == {"id": 5, "name": "Frontend"}
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "INNER JOIN team_user" in args[0]


@patch("repository.team_repository.pymysql.connect")
def test_get_by_id_not_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchone.return_value = None
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_by_id(999)

    assert result == []


# --- insert -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_insert_creates_team_and_statuses(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.lastrowid = 10
    mock_connect.return_value = conn

    repo = TeamRepository()
    data = {"name": "Analytics", "description": "Data team"}

    result = repo.insert(data)

    assert result == 10
    assert cur.execute.call_count >= 2
    conn.commit.assert_called_once()


# --- get_team_statuses ------------------------


@patch("repository.team_repository.pymysql.connect")
def test_get_team_statuses(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [
        {"id": 1, "name": "TODO"},
        {"id": 2, "name": "IN PROGRESS"},
    ]
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.get_team_statuses(1)

    assert len(result) == 2
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "FROM statuses" in args[0]


# --- update -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_update_team(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TeamRepository()
    data = {"id": 7, "name": "SecOps", "description": "Security team"}
    result = repo.update(data, "user-1")

    assert result == 1
    conn.commit.assert_called_once()
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "UPDATE teams" in args[0]


# --- delete -------------------------------------------------------------------


@patch("repository.team_repository.pymysql.connect")
def test_delete_team(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TeamRepository()
    result = repo.delete(99)

    assert result == 1
    conn.commit.assert_called_once()
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "DELETE FROM teams" in args[0]
