from flask import Flask
from flask_restful import Api

from health.health_controller import Health
from login.login_controller import Login

def build_app():
    app = Flask(__name__)
    api = Api(app)

    api.add_resource(Health, '/health', endpoint='health')
    api.add_resource(Login, '/login', endpoint='login')
    
    return app


if __name__ == '__main__':
    app = build_app()
    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )
