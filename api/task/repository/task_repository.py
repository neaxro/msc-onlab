import pymysql
import pymysql.cursors
from utils.config import Config


class TaskRepository:
    def __init__(self):
        config = Config()

        self.connection = pymysql.connect(
            host=config.MYSQL_HOST,
            user=config.MYSQL_USER,
            password=config.MYSQL_PASSWORD,
            database=config.MYSQL_DATABASE,
            port=config.MYSQL_PORT,
        )

    def get_all(self, team_id, user_id=None):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            if user_id:
                cur.execute(
                    """
                    SELECT * FROM tasks t
                    WHERE
                        t.team_id = %s AND
                        t.responsible_id = %s
                    """,
                    (
                        team_id,
                        user_id,
                    ),
                )
            else:
                cur.execute(
                    """
                    SELECT * FROM tasks t
                    WHERE
                        t.team_id = %s
                    """,
                    (team_id,),
                )
            result = cur.fetchall()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
