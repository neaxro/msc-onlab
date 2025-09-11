import logging

from controllers.health_controller import Health
from controllers.login_controller import Login
from controllers.register_controller import Register
from controllers.user_controller import User
from controllers.user_finder import UserFinder
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
    api.add_resource(Health, "/health", endpoint="health")
    api.add_resource(Login, "/login", endpoint="login")
    api.add_resource(Register, "/register", endpoint="register")
    api.add_resource(User, "/user/<user_id>", endpoint="user")
    api.add_resource(UserFinder, "/user-search/<username>", endpoint="userfinder")

    return app


application = build_app()

if __name__ == "__main__":
    config = Config()

    app.run(host=config.APP_HOST, port=config.APP_PORT, debug=config.APP_DEBUG)
