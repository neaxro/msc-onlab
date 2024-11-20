from flask_restful import Resource

class Health(Resource):
    def __init__(self):
        self.asd = "asd"

    def get(self):
        return "Healthy", 200
