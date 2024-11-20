from flask_restful import Resource

class Health(Resource):
    def __init__(self):
        pass

    def get(self):
        return "Healthy", 200
