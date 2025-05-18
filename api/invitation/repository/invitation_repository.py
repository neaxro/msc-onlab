import pymysql
import pymysql.cursors
from utils.config import Config


class InvitationRepository:
    def __init__(self):
        config = Config()

        self.connection = pymysql.connect(
            host=config.MYSQL_HOST,
            user=config.MYSQL_INVITATION_USER,
            password=config.MYSQL_INVITATION_PASSWORD,
            database=config.MYSQL_DATABASE,
            port=config.MYSQL_PORT,
        )

    def get_pending_for_user(self, user_id):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT id, team_id, token, email, expires, created_at, inviter_user_id
                FROM invitations
                WHERE invited_user_id = %s AND accepted IS NULL AND expires > NOW()
                """,
                (user_id,),
            )
            return cur.fetchall()
        finally:
            cur.close()

    def insert(self, invited_user_id, inviter_user_id, team_id, expires, token, email):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                INSERT INTO invitations (invited_user_id,
                    inviter_user_id, team_id, expires, token, email)
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

    def update_invitation_status(self, token, accepted: bool):
        try:
            cur = self.connection.cursor()
            if accepted:
                # Set accepted timestamp
                cur.execute(
                    """
                    UPDATE invitations SET accepted = NOW()
                    WHERE token = %s
                        AND accepted IS NULL AND expires > NOW()
                    """,
                    (token,),
                )
            else:
                # Delete the invitation
                cur.execute(
                    """
                    DELETE FROM invitations
                    WHERE token = %s
                        AND accepted IS NULL
                        AND expires > NOW()
                    """,
                    (token,),
                )

            self.connection.commit()
            return cur.rowcount
        except Exception:
            self.connection.rollback()
            raise
        finally:
            cur.close()

    def get_invitation_by_token(self, token):
        try:
            cur = self.connection.cursor(pymysql.cursors.DictCursor)
            cur.execute(
                """
                SELECT *
                FROM invitations
                WHERE token = %s
                    AND accepted IS NULL
                    AND expires > NOW()
                """,
                (token,),
            )
            return cur.fetchone()
        finally:
            cur.close()
