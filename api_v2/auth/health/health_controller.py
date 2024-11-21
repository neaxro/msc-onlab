from flask_restful import Resource
from metrics import count_requests, latency_request, time_request


class Health(Resource):
    def __init__(self):
        pass

    @count_requests
    @time_request
    @latency_request
    def get(self):
        return "Healthy", 200
