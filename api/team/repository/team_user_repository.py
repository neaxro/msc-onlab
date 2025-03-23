import pymysql
import pymysql.cursors
from utils.config import Config


class TeamUserRepository:
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
            cur.execute("SELECT * FROM team_user")
            result = cur.fetchall()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def insert(self, team_id, user_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO team_user (team_id, user_id)
                VALUES (%s, %s)
                """,
                (team_id, user_id),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def delete(self, team_id, user_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                DELETE FROM team_user
                WHERE team_id=%s and user_id=%s
                """,
                (team_id, user_id),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def is_user_part_of_team(self, team_id, user_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            result = cur.execute(
                """
                SELECT * FROM team_user
                WHERE team_id = %s and user_id = %s
                """,
                (team_id, user_id),
            )

            self.connection.commit()

            if not result:
                return None
            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
