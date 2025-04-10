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

    def modify(
        self,
        task_id,
        title=None,
        description=None,
        due_date=None,
        status_id=None,
        responsible_id=None,
    ):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)

            fields = []
            values = []

            if title is not None:
                fields.append("title = %s")
                values.append(title)
            if description is not None:
                fields.append("description = %s")
                values.append(description)
            if due_date is not None:
                fields.append("due_date = %s")
                values.append(due_date)
            if status_id is not None:
                fields.append("status_id = %s")
                values.append(status_id)
            if responsible_id is not None:
                fields.append("responsible_id = %s")
                values.append(responsible_id)

            if not fields:
                raise ValueError("No fields provided to update.")

            values.append(task_id)

            sql = f"""
                UPDATE tasks
                SET {', '.join(fields)}
                WHERE id = %s
            """

            cur.execute(sql, values)
            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def delete(self, task_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                DELETE FROM tasks
                WHERE id =%s
                """,
                (task_id,),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
