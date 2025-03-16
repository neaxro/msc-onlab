from api.team.utils.config import Config
from api.team.utils.db_migration import get_backend, read_migrations


def apply_migration():
    config = Config()

    connection_string = "mysql://{user}:{password}@{host}:{port}/{dbname}".format(
        user=config.MYSQL_TEAM_USER,
        password=config.MYSQL_TEAM_PASSWORD,
        host=config.MYSQL_HOST,
        port=config.MYSQL_PORT,
        dbname=config.MYSQL_DATABASE,
    )

    backend = get_backend(connection_string)
    migrations = read_migrations("./migrations")
    backend.apply_migrations(backend.to_apply(migrations))
