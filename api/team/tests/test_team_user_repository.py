from unittest.mock import MagicMock, patch

import pytest
from repository.team_user_repository import TeamUserRepository


@pytest.fixture
def mock_connection():
    conn = MagicMock()
    cur = MagicMock()
    conn.cursor.return_value = cur
    return conn, cur


# --- get_users_teams -------------------------------------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_get_users_teams(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": 1, "name": "Infra"}]
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    result = repo.get_users_teams("user123")

    assert result == [{"id": 1, "name": "Infra"}]
    cur.execute.assert_called_once()

    args, kwargs = cur.execute.call_args

    assert "FROM team_user" in args[0]
    assert args[1] == ("user123",)


# --- get_team_users -------------------------------------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_get_team_users(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.fetchall.return_value = [{"id": "user1", "role": "ADMIN"}]
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    result = repo.get_team_menbers(10)

    assert result == [{"id": "user1", "role": "ADMIN"}]
    cur.execute.assert_called_once()
    args, kwargs = cur.execute.call_args
    assert "WHERE tu.team_id" in args[0]


# --- add_user_to_team -------------------------------------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_add_user_to_team(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    result = repo.insert("user123", 7)

    assert result == 1
    conn.commit.assert_called_once()
    cur.execute.assert_called_once()

    args, kwargs = cur.execute.call_args
    assert "INSERT INTO team_user" in args[0]

    assert args[1] == ("user123", 7)


# --- add_user_to_team_existing (duplicate entry) ---------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_insert_duplicate(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.execute.side_effect = Exception("Duplicate entry")
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    with pytest.raises(Exception, match="Duplicate entry"):
        repo.insert("user123", 7)


# --- remove_user_from_team -------------------------------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_delete_user_from_team(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 1
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    result = repo.delete(7, "user123")

    assert result == 1
    cur.execute.assert_called_once()
    conn.commit.assert_called_once()

    args, kwargs = cur.execute.call_args
    assert "DELETE FROM team_user" in args[0]
    assert args[1] == (7, "user123")


# --- remove_user_from_team_not_found ---------------------------------------------


@patch("repository.team_user_repository.pymysql.connect")
def test_delete_user_from_team_not_found(mock_connect, mock_connection):
    conn, cur = mock_connection
    cur.rowcount = 0
    mock_connect.return_value = conn

    repo = TeamUserRepository()
    result = repo.delete(99, "user404")

    assert result == 0
    cur.execute.assert_called_once()
    conn.commit.assert_called_once()

    args, kwargs = cur.execute.call_args
    assert "DELETE FROM team_user" in args[0]
    assert args[1] == (99, "user404")
