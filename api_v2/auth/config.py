import os

class Config:
    def __init__(self):
        # Flask app related config
        self.APP_HOST = os.getenv("APP_HOST", "0.0.0.0")
        self.APP_PORT = os.getenv("APP_PORT", 5000)
        self.APP_DEBUG = os.getenv("APP_DEBUG", "True") == "True"
        
        # Keycloak related config
        self.KEYCLOAK_SERVER_URL = os.getenv("KEYCLOAK_SERVER_URL", "http://localhost:8080")
        self.KEYCLOAK_REALM_NAME = os.getenv("KEYCLOAK_REALM_NAME", "msc-onlab-test")
        self.KEYCLOAK_CLIENT_ID = os.getenv("KEYCLOAK_CLIENT_ID", "msc-onlab-auth-microservice-client-test")
        self.KEYCLOAK_CLIENT_SECRET = os.getenv("KEYCLOAK_CLIENT_SECRET", "moo8oexa0Aitoon8chohCaeh8eith5ei")
