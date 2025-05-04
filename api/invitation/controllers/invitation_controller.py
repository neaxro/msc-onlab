from flask_restful import Resource
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import requires_auth


class InvitationController(Resource):
    def __init__(self):
        pass

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self):
        return "Hello"
