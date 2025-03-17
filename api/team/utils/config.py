import os


class Config:
    def __init__(self):
        # Flask app related config
        self.APP_HOST = os.getenv("APP_HOST", "0.0.0.0")
        self.APP_PORT = os.getenv("APP_PORT", 5001)
        self.APP_DEBUG = os.getenv("APP_DEBUG", "True") == "True"

        # Keycloak related config
        self.KEYCLOAK_SERVER_URL = os.getenv(
            "KEYCLOAK_SERVER_URL", "http://localhost:8080"
        )
        self.KEYCLOAK_REALM_NAME = os.getenv("KEYCLOAK_REALM_NAME", "msc-onlab-test")
        self.KEYCLOAK_CLIENT_ID = os.getenv(
            "KEYCLOAK_CLIENT_ID", "msc-onlab-microservice-client-test"
        )
        self.KEYCLOAK_CLIENT_SECRET = os.getenv(
            "KEYCLOAK_CLIENT_SECRET", "moo8oexa0Aitoon8chohCaeh8eith5ei"
        )

        # Metrics related config
        self.METRICS_PREFIX = os.getenv("METRICS_PREFIX", "msc_onlab")
        self.METRICS_APP_NAME = os.getenv("METRICS_APP_NAME", "team")

        # Logging related
        self.TIMEZONE = os.getenv("TIMEZONE", "Europe/Budapest")

        # MySQL database related config
        self.MYSQL_TEAM_USER = os.getenv("MYSQL_USER", "team_service_user")
        self.MYSQL_TEAM_PASSWORD = os.getenv("MYSQL_PASSWORD", "pass")
        self.MYSQL_DATABASE = os.getenv("MYSQL_DATABASE", "msc_onlab")
        self.MYSQL_TEAM_TABLE = os.getenv("MYSQL_TEAM_TABLE", "teams")
        self.MYSQL_HOST = os.getenv("MYSQL_HOST", "localhost")
        self.MYSQL_PORT = int(os.getenv("MYSQL_PORT", 3306))
