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

    def get_all(self, user_id=None):
        """
        Finds all team where user is member.

        :param str user_id: Id of the user, or none for all team search
        :return: Teams' data
        :rtype: list
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            if user_id:
                cur.execute(
                    """
                    SELECT t.id , t.name ,t.description  from teams t
                    inner join team_user tu on tu.team_id =t.id
                    WHERE tu.user_id = %s
                    """,
                    (user_id,),
                )
            else:
                cur.execute(
                    """
                    SELECT t.id , t.name ,t.description  from teams t
                    """,
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

    def get_by_id(self, team_id, user_id=None):
        """
        Finds the team by id.

        :param int team_id: Id of the team
        :param str user_id: Id of the user or None if whole db search is needed.
        :return: Team's data or None if not found
        :type priority: object or []
        :rtype: dict
        """
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            if user_id:
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
            else:
                cur.execute(
                    """
                    SELECT t.id , t.name ,t.description  from teams t
                    WHERE
                        t.id = %s
                    """,
                    (team_id,),
                )
            result = cur.fetchone()

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

    def update(self, data, user_id):
        """
        Updates team based on new team data.

        :param dict data: The new data which overwrites the existing team.
        :return: The affected row count
        :rtype: int
        """

        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                UPDATE teams t
                SET name = %(name)s, description = %(description)s
                WHERE t.id = %(id)s
                AND EXISTS (
                    SELECT 1
                    FROM team_user tu
                    WHERE tu.team_id = t.id
                    AND tu.user_id = %(user_id)s
                );
                """,
                {
                    "name": data["name"],
                    "description": data["description"],
                    "id": data["id"],
                    "user_id": user_id,
                },
            )

            self.connection.commit()

            return cur.rowcount
        except Exception as e:
            self.connection.rollback()
            raise e
        finally:
            cur.close()

    def delete(self, team_id):
        """
        Deletes team with team_id.
        It also deletes any related records in team_user table!

        :param int team_id: The Id of the team that need to be deleted
        :return: The affected row count
        :rtype: int
        """
        try:
            # Cascade deletion on team_user table!
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
