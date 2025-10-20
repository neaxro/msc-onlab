from unittest.mock import MagicMock, patch

from repository.invitation_repository import InvitationRepository


@patch("repository.invitation_repository.pymysql.connect")
def test_invitation_repository_insert(mock_connect):
    mock_conn = MagicMock()
    mock_cur = MagicMock()
    mock_cur.rowcount = 1
    mock_conn.cursor.return_value = mock_cur
    mock_connect.return_value = mock_conn

    repo = InvitationRepository()
    result = repo.insert("user2", "user1", 1, "2025-01-01 00:00:00", "token", "a@b.com")

    assert result == 1
    mock_cur.execute.assert_called()
    mock_conn.commit.assert_called_once()


@patch("repository.invitation_repository.pymysql.connect")
def test_invitation_repository_get_pending_for_user(mock_connect):
    mock_conn = MagicMock()
    mock_cur = MagicMock()
    mock_cur.fetchall.return_value = [{"id": 1}]
    mock_conn.cursor.return_value = mock_cur
    mock_connect.return_value = mock_conn

    repo = InvitationRepository()
    result = repo.get_pending_for_user("user2")

    assert result == [{"id": 1}]
    mock_cur.execute.assert_called()
