import datetime
from flask import request, jsonify
from flask_restful import Resource
import jwt, json, os
from functools import wraps

# Decorator to check if the request is authorized
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        secret_key = os.environ['TOKEN_SECRET_KEY']

        if not token:
            return jsonify({'message': 'Token is missing'})

        try:
            data = jwt.decode(token.split(' ')[1], secret_key, algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            return jsonify({'message': 'Token has expired'})
        except jwt.InvalidTokenError:
            return jsonify({'message': 'Invalid token'})

        return f(*args, **kwargs, user_data=data)

    return decorated
