from flask import Flask
from flask_restful import Api
from config import Config
from health.health_controller import Health
from login.login_controller import Login

def build_app():
    app = Flask(__name__)
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
