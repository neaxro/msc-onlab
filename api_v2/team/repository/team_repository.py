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

    def get_all(self, user_id):
        """
        Finds all team where user is member.

        :param str user_id: Id of the user
        :return: Teams' data
        :rtype: list
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT t.id , t.name ,t.description  from teams t
                inner join team_user tu on tu.team_id =t.id
                WHERE tu.user_id = %s
                """,
                (user_id,),
            )
            result = cur.fetchall()

            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def get_by_name(self, team_name):
        """
        Finds the team by its name.

        :param str team_name: Name of the team
        :return: Team's data or None if not found
        :type priority: object or None
        :rtype: dict
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute("SELECT * FROM teams WHERE name = %s", (team_name,))
            result = cur.fetchall()

            if not result:
                return None  # No team found with the given name
            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def get_by_id(self, team_id, user_id):
        """
        Finds the team by id.

        :param int team_id: Id of the team
        :return: Team's data or None if not found
        :type priority: object or []
        :rtype: dict
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT t.id , t.name ,t.description  from teams t
                INNER JOIN team_user tu on tu.team_id =t.id
                WHERE
                    t.id = %s AND
                    tu.user_id = %s
                """,
                (
                    team_id,
                    user_id,
                ),
            )
            result = cur.fetchall()

            if not result:
                return []  # No team found with the given name
            return result
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def insert(self, data):
        """
        Inserts a new Team.

        :param object data: The object that contains team's data (name, description)
        :return: The id of the inserted team.
        :rtype: int
        """
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

    def delete(self, team_id):
        """
        Deletes team with team_id

        :param int team_id: The Id of the team that need to be deleted
        :return: The affected row count
        :rtype: int
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                DELETE FROM teams
                WHERE id =%s
                """,
                (team_id,),
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()
