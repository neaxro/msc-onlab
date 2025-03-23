from flask import current_app
from flask_restful import Resource
from utils.metrics import count_requests, latency_request, time_request


class HealthController(Resource):
    def __init__(self):
        pass

    @count_requests
    @time_request
    @latency_request
    def get(self):
        current_app.logger.info("Sending health signal.")
        return "Healthy", 200
