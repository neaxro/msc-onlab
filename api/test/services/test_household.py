import pytest
from bson import ObjectId

from test.environment.database import MockedDatabase
from app.db.app_database import AppDatabase
from app.user.user_service import UserService

from app.household.household_service import HouseholdService

@pytest.fixture()
def database() -> AppDatabase:
    return MockedDatabase()

@pytest.fixture()
def service(database) -> HouseholdService:
    return HouseholdService(database)

# --------------------------------------------
# |                 TESTS                    |
# --------------------------------------------

def test_get_all(service: HouseholdService):
    households = service.get_all()
        
    assert len(households) > 0
    assert len(households) == 2

def test_get_all_brief(service: HouseholdService):
    households = service.get_all_brief()
        
    assert len(households) > 0
    assert len(households) == 2
    
def test_get_by_id_found_1(service: HouseholdService):
    household = service.get_by_id("65dcf20484273f26b75734b0")
    
    assert household is not None

def test_get_by_id_found_2(service: HouseholdService):
    household = service.get_by_id("65dcf20484273f26b75734b1")
    
    assert household is not None

def test_get_by_id_not_found(service: HouseholdService):
    household = None
    try:
        household = service.get_by_id("65dcf20484273f26b75734b9")
    except Exception:
        assert household is None

def test_insert_household(service: HouseholdService):
    new_household = {
        "title": "Otthon"
    }
    
    user_token = {}
    user_token['id'] = ObjectId("65dcf20484273f26b75734f0")
    
    
    result = service.insert_household(new_household, user_token)
    
    assert result.inserted_id

def test_insert_household_fail(service: HouseholdService):
    with pytest.raises(Exception) as e_info:
        new_household = {
            "title": "Otthon"
        }
        
        user_token = {}
        user_token['id'] = ObjectId("invalid_objectid")
        
        
        result = service.insert_household(new_household, user_token)
