from pymongo import MongoClient
from pymongo.database import Database
from pymongo.collection import Collection
from abc import ABC, abstractmethod

class AppDatabase(ABC):
    
    @property
    @abstractmethod
    def client(self) -> MongoClient:
        raise NotImplementedError

    @property
    @abstractmethod
    def db(self) -> Database:
        raise NotImplementedError
    
    @property
    @abstractmethod
    def user_collection(self) -> Collection:
        raise NotImplementedError
    
    @property
    @abstractmethod
    def household_collection(self) -> Collection:
        raise NotImplementedError
