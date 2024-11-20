from flask_restful import Resource

class Login(Resource):
    def __init__(self):
        self.asd = "asd"

    def get(self):
        return "Login"