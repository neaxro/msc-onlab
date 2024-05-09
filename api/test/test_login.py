from flask import Flask
from flask_restful import Api
import pytest

from test.environment.database import MockedDatabase

from app.db.app_database import AppDatabase
from app.user.user_service import UserService
from app.utils.time_management import utcnow

@pytest.fixture()
def app():
    app = Flask(__name__)
    api = Api(app)
    app.config.update({
        "TESTING": True,
    })

    # other setup can go here

    yield app

    # clean up / reset resources here

@pytest.fixture()
def database() -> AppDatabase:
    return MockedDatabase()

@pytest.fixture()
def client(app):
    return app.test_client()

@pytest.fixture()
def runner(app):
    return app.test_cli_runner()

# --------------------------------------------
# |                 TESTS                    |
# --------------------------------------------

def test_dummy_1(client):
    sum = 1 + 1
    assert sum == 2

def test_dummy_2(client):
    sum = 1 + 2
    assert sum > 2

def test_json_validation(client):
    
    date = utcnow()
    assert date is not None

def test_user_service_1(database):
    user_service = UserService(database)
    
    user = user_service.get_user_by_id("65dcf20484273f26b75734f0")
    
    assert user is not None

def test_user_service_2(database):
    user_service = UserService(database)
    
    user = user_service.get_user_by_id("65dcf20484273f26b75734f9")
    
    assert user is None
