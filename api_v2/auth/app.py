from flask import Flask
from flask_restful import Api
from werkzeug.middleware.dispatcher import DispatcherMiddleware
from prometheus_client import make_wsgi_app
from config import Config
from health.health_controller import Health
from login.login_controller import Login

def build_app():
    app = Flask(__name__)
    app.wsgi_app = DispatcherMiddleware(app.wsgi_app, {
        '/metrics': make_wsgi_app()
    })

    api = Api(app)
    api.add_resource(Health, '/health', endpoint='health')
    api.add_resource(Login, '/login', endpoint='login')
    
    return app


if __name__ == '__main__':
    config = Config()
    
    app = build_app()
    app.run(
        host=config.APP_HOST,
        port=config.APP_PORT,
        debug=config.APP_DEBUG
    )
