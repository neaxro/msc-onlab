import datetime
from flask import Flask, jsonify, request
from flask_restful import Resource, Api
import jwt
from functools import wraps

app = Flask(__name__)
api = Api(app)

# Replace this with a secure secret key in a production environment
app.config['SECRET_KEY'] = 'your_secret_key'

# Sample user data (replace with your actual user authentication logic)
users = {
    'username': 'password',
}

# Decorator to check if the request is authorized
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')

        if not token:
            return jsonify({'message': 'Token is missing'}), 401

        try:
            data = jwt.decode(token.split(' ')[1], app.config['SECRET_KEY'], algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            return jsonify({'message': 'Token has expired'}), 401
        except jwt.InvalidTokenError:
            return jsonify({'message': 'Invalid token'}), 401

        # You can now use 'data' to access user information in your API endpoint
        # For example, you can check if the user has the necessary permissions
        # based on the 'data' dictionary.

        return f(*args, **kwargs, user_data=data)

    return decorated

class ProtectedResource(Resource):
    @token_required
    def get(self, user_data):
        return jsonify({'message': 'This is a protected resource', 'user_data': user_data})

class TokenResource(Resource):
    def post(self):
        data = request.get_json()

        if 'username' in data and 'password' in data:
            username = data['username']
            password = data['password']

            if users.get(username) == password:
                # Generate a JWT token
                expiration_time = datetime.datetime.utcnow() + datetime.timedelta(hours=1)
                token = jwt.encode({'username': username, 'exp': expiration_time}, app.config['SECRET_KEY'], algorithm='HS256')
                
                return {'token': token}

        return {'message': 'Invalid credentials'}, 401

api.add_resource(TokenResource, '/token')
api.add_resource(ProtectedResource, '/protected')

if __name__ == '__main__':
    app.run(debug=True)
