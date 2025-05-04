import pymysql
import pymysql.cursors
from utils.config import Config


class SubtaskRepository:
    def __init__(self):
        config = Config()

        self.connection = pymysql.connect(
            host=config.MYSQL_HOST,
            user=config.MYSQL_USER,
            password=config.MYSQL_PASSWORD,
            database=config.MYSQL_DATABASE,
            port=config.MYSQL_PORT,
        )

    def get_by_id(self, subtask_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT *
                FROM subtasks s
                WHERE s.id = %s
                """,
                (subtask_id,),
            )

            result = cur.fetchone()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def get_all(self, task_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT title, done
                FROM subtasks s
                WHERE s.task_id = %s
                """,
                (task_id,),
            )

            result = cur.fetchall()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def insert(self, task_id, title, done):
        """
        Inserts a subtask to an existing task.
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO subtasks (title, done, task_id)
                VALUES (%s, %s, %s)
                """,
                (title, done, task_id),
            )
            return cur.lastrowid
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def modify(
        self,
        subtask_id,
        title=None,
        done=None,
    ):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)

            fields = []
            values = []

            if title is not None:
                fields.append("title = %s")
                values.append(title)
            if done is not None:
                fields.append("done = %s")
                values.append(done)

            if not fields:
                raise ValueError("No fields provided to update.")

            values.append(subtask_id)

            sql = f"""
                UPDATE subtask_id
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

    def delete(self, subtask_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                DELETE FROM subtasks
                WHERE id =%s
                """,
                (subtask_id,),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
