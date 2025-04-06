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
                    SELECT t.id, t.title, t.description, t.creation_date,
                        t.due_date, t.responsible_id, t.team_id,
                        s.name as status
                    FROM tasks t
                    INNER JOIN statuses s ON s.id = t.status_id
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
                    SELECT t.id, t.title, t.description, t.creation_date,
                        t.due_date, t.responsible_id, t.team_id,
                        s.name as status
                    FROM tasks t
                    INNER JOIN statuses s ON s.id = t.status_id
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

    def get_by_id(self, task_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT t.id, t.title, t.description, t.creation_date,
                    t.due_date, t.responsible_id, t.team_id,
                    s.name as status
                FROM tasks t
                INNER JOIN statuses s ON s.id = t.status_id
                WHERE t.id = %s
                """,
                (task_id,),
            )

            result = cur.fetchone()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def insert(self, title, description, due_date, responsible_id, team_id, status_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO tasks (title, description, creation_date, due_date, responsible_id, team_id, status_id)  # noqa: E501
                VALUES (%s, %s, NOW(), %s, %s, %s, %s)
                """,
                (title, description, due_date, responsible_id, team_id, status_id),
            )

            self.connection.commit()

            return cur.lastrowid
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
