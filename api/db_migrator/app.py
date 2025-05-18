import logging
from time import time

from retry import retry
from utils.config import Config
from yoyo import get_backend, read_migrations

logger = logging.getLogger("db_migration")


@retry(Exception, tries=5, delay=2, backoff=2)
def apply_migration():
    config = Config()

    connection_string = "mysql://{user}:{password}@{host}:{port}/{dbname}".format(
        user=config.MYSQL_USER,
        password=config.MYSQL_PASSWORD,
        host=config.MYSQL_HOST,
        port=config.MYSQL_PORT,
        dbname=config.MYSQL_DATABASE,
    )

    backend = get_backend(connection_string, migration_table="yoyo_migrations_tasks")
    migrations = read_migrations("./migrations")
    backend.apply_migrations(backend.to_apply(migrations))


def main():
    logging.basicConfig(level=logging.INFO)
    logger.info("Starting database migration...")

    # Start and measure database migration
    start_time = time()
    apply_migration()
    end_time = time()

    logger.info("Migration finished in %.3f seconds!" % (end_time - start_time))
    logger.info("Exiting...")


if __name__ == "__main__":
    main()

# Trigger worklow...
