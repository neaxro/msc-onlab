import logging

from controllers.health.health_controller import HealthController
from controllers.team.membership_controller import MembershipController
from controllers.team.team_controller import TeamController
from controllers.team.team_info_controller import TeamInfoController
from flask import Flask
from flask_restful import Api
from prometheus_client import make_wsgi_app
from utils.config import Config
from utils.logging_config import setup_logger
from werkzeug.middleware.dispatcher import DispatcherMiddleware

app = Flask(__name__)

logging.basicConfig(level=logging.INFO)
app.logger.setLevel(logging.INFO)


def build_app():
    setup_logger(app)
    app.wsgi_app = DispatcherMiddleware(app.wsgi_app, {"/metrics": make_wsgi_app()})

    api = Api(app)
    api.add_resource(HealthController, "/health", endpoint="health")
    api.add_resource(TeamController, "/teams", "/teams/<team_id>", endpoint="team")
    api.add_resource(TeamInfoController, "/teams/<team_id>/info", endpoint="teaminfo")
    api.add_resource(
        MembershipController,
        "/membership/<team_id>/invited/<invited_user_id>",
        "/membership/<team_id>/members",
        "/membership/<user_id>/teams",
        endpoint="membership",
    )

    return app


application = build_app()

if __name__ == "__main__":
    config = Config()

    app.run(host=config.APP_HOST, port=config.APP_PORT, debug=config.APP_DEBUG)
