import pymysql
import pymysql.cursors
from utils.config import Config


class InvitationRepository:
    def __init__(self):
        config = Config()

        self.connection = pymysql.connect(
            host=config.MYSQL_HOST,
            user=config.MYSQL_TEAM_USER,
            password=config.MYSQL_TEAM_PASSWORD,
            database=config.MYSQL_DATABASE,
            port=config.MYSQL_PORT,
        )

    def insert(self, invited_user_id, inviter_user_id, team_id, expires, token, email):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO invitations (invited_user_id, inviter_user_id,
                    team_id, expires, token, email)
                VALUES (%s, %s, %s, %s, %s, %s)
                """,
                (invited_user_id, inviter_user_id, team_id, expires, token, email),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
