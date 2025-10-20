from unittest.mock import MagicMock

import pytest
from service.auth_service import AuthService


@pytest.fixture
def mock_keycloak_repo():
    return MagicMock()


@pytest.fixture
def auth_service(mock_keycloak_repo):
    return AuthService(mock_keycloak_repo)


def test_login_success(auth_service, mock_keycloak_repo):
    mock_keycloak_repo.login.return_value = {"access_token": "token"}
    data = {"username": "john", "password": "secret"}

    token = auth_service.login(data)

    assert token == {"access_token": "token"}
    mock_keycloak_repo.login.assert_called_once_with("john", "secret")


def test_login_missing_fields(auth_service):
    with pytest.raises(Exception, match="Username is required"):
        auth_service.login({"password": "secret"})
    with pytest.raises(Exception, match="Password is required"):
        auth_service.login({"username": "john"})


def test_register_success(auth_service, mock_keycloak_repo):
    mock_keycloak_repo.register.return_value = "user-id-123"
    data = {
        "first_name": "John",
        "last_name": "Doe",
        "email": "john@example.com",
        "username": "johndoe",
        "password": "secret",
        "profile_picture": "avatar.png",
    }

    user_id = auth_service.register(data)
    assert user_id == "user-id-123"
    mock_keycloak_repo.register.assert_called_once()


def test_register_missing_fields(auth_service):
    data = {"username": "johndoe"}
    with pytest.raises(Exception, match="Missing fields"):
        auth_service.register(data)


def test_get_user_by_id(auth_service, mock_keycloak_repo):
    mock_keycloak_repo.get_user_by_id.return_value = {"id": "1"}
    user = auth_service.get_user_by_id("1")
    assert user["id"] == "1"


def test_get_user_by_name(auth_service, mock_keycloak_repo):
    mock_keycloak_repo.get_user_by_username.return_value = {"username": "john"}
    user = auth_service.get_user_by_name("john")
    assert user["username"] == "john"


def test_modify_user_success(auth_service, mock_keycloak_repo):
    user_data = {"username": "john"}
    auth_service.get_user_by_id = MagicMock(return_value=user_data)
    mock_keycloak_repo.modify_user.return_value = True

    data = {
        "username": "john",
        "first_name": "John",
        "last_name": "Doe",
        "email": "john@example.com",
        "password": "secret",
        "profile_picture": "avatar.png",
    }

    result = auth_service.modify_user("1", data)
    assert result is True


def test_modify_user_invalid_username(auth_service):
    auth_service.get_user_by_id = MagicMock(return_value={"username": "alice"})
    data = {
        "username": "bob",
        "first_name": "B",
        "last_name": "B",
        "email": "b@b.com",
        "password": "pass",
        "profile_picture": "pic",
    }
    with pytest.raises(Exception, match="cannot modify other users"):
        auth_service.modify_user("1", data)


def test_enable_disable_user(auth_service, mock_keycloak_repo):
    mock_keycloak_repo.enable_user.return_value = True
    mock_keycloak_repo.disable_user.return_value = True

    assert auth_service.enable_user("1") is True
    assert auth_service.disable_user("1") is True
