from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.auth_service_repository import AuthServiceRepository
from repository.invitation_repository import InvitationRepository
from repository.team_repository import TeamRepository
from service.invitation_service import InvitationService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class InvitationController(Resource):
    """Controller for handling invitation-related API requests."""

    def __init__(self):
        self.invitation_service = InvitationService(
            InvitationRepository(), AuthServiceRepository(), TeamRepository()
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self):
        return "Hello"

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def post(self):
        """Handles creating a new task."""
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")
            auth_header = request.headers.get("Authorization", None)

            data = request.get_json()

            new_invitation_id = self.invitation_service.insert(
                data, user_data["sub"], auth_header
            )

            current_app.logger.info(f"Invitation created successfully by {username}.")

            response = jsonify(
                {
                    "message": "New invitation successfully created!",
                    "invitation_id": new_invitation_id,
                }
            )
            response.status_code = 201

            return response
        except Exception as e:
            return self.handle_error(f"Error creating invitation: {str(e)}", 500)

    @staticmethod
    def handle_error(message, status_code):
        """Logs and returns a formatted error response."""
        current_app.logger.error(message)
        return {"error": message}, status_code
