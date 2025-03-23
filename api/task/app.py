import logging

from controllers.health.health_controller import HealthController
from controllers.task.task_controller import TaskController
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
    api.add_resource(TaskController, "/tasks", "/tasks/<task_id>", endpoint="task")

    return app


application = build_app()

if __name__ == "__main__":
    config = Config()

    app.run(host=config.APP_HOST, port=config.APP_PORT, debug=config.APP_DEBUG)
