import pymysql
import pymysql.cursors
from utils.config import Config


class TeamRepository:
    def __init__(self):
        config = Config()

        self.connection = pymysql.connect(
            host=config.MYSQL_HOST,
            user=config.MYSQL_TEAM_USER,
            password=config.MYSQL_TEAM_PASSWORD,
            database=config.MYSQL_DATABASE,
            port=config.MYSQL_PORT,
        )

    def get_all(self):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute("SELECT * FROM teams")
            result = cur.fetchall()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def insert(self, data):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO teams (name, description)
                VALUES (%(name)s, %(description)s)
                """,
                data,
            )

            self.connection.commit()

            return cur.lastrowid
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
