import os


class Config:
    def __init__(self):
        # Logging related
        self.TIMEZONE = os.getenv("TIMEZONE", "Europe/Budapest")

        # MySQL database related config
        self.MYSQL_MIGRATOR_USER = os.getenv(
            "MYSQL_MIGRATOR_USER", "db_migrator_service_user"
        )
        self.MYSQL_MIGRATOR_PASSWORD = os.getenv("MYSQL_MIGRATOR_PASSWORD", "pass")
        self.MYSQL_DATABASE = os.getenv("MYSQL_DATABASE", "msc_onlab")
        self.MYSQL_HOST = os.getenv("MYSQL_HOST", "localhost")
        self.MYSQL_PORT = int(os.getenv("MYSQL_PORT", 3306))
