from flask_restful import Resource
from metrics import Metrics, count_requests, time_request, latency_request


class Health(Resource):
    def __init__(self):
        pass

    @count_requests
    @time_request
    @latency_request
    def get(self):
        return "Healthy", 200
