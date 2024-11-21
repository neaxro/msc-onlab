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
        self.KEYCLOAK_ADMIN_USERNAME = os.getenv("KEYCLOAK_ADMIN_USERNAME", "realmadmin")
        self.KEYCLOAK_ADMIN_PASSWORD = os.getenv("KEYCLOAK_ADMIN_PASSWORD", "Asdasd11")
        
        # Metrics related config
        self.METRICS_PREFIX = os.getenv("METRICS_PREFIX", "msc_onlab")
        self.METRICS_APP_NAME = os.getenv("METRICS_APP_NAME", "auth")
