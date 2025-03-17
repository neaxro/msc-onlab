import logging

from controllers.health.health_controller import Health
from controllers.team.team_controller import Team
from flask import Flask
from flask_restful import Api
from prometheus_client import make_wsgi_app
from utils.config import Config
from utils.db_migration import apply_migration
from utils.logging_config import setup_logger
from werkzeug.middleware.dispatcher import DispatcherMiddleware

app = Flask(__name__)

logging.basicConfig(level=logging.INFO)
app.logger.setLevel(logging.INFO)


def build_app():
    apply_migration()

    setup_logger(app)
    app.wsgi_app = DispatcherMiddleware(app.wsgi_app, {"/metrics": make_wsgi_app()})

    api = Api(app)
    api.add_resource(Health, "/health", endpoint="health")
    api.add_resource(Team, "/teams", "/teams/<team_id>", endpoint="team")

    return app


application = build_app()

if __name__ == "__main__":
    config = Config()

    app.run(host=config.APP_HOST, port=config.APP_PORT, debug=config.APP_DEBUG)
