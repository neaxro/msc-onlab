from app import app, api
from app.auth.login.login_routes import LoginResource

api.add_resource(LoginResource, '/auth/login')

if __name__ == '__main__':
    app.run(debug=True)