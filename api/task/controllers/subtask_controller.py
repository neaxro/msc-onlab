from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.auth_service_repository import AuthServiceRepository
from repository.subtask_repository import SubtaskRepository
from repository.task_repository import TaskRepository
from service.subtask_service import SubtaskService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class SubtaskController(Resource):
    """Controller for handling subtask-related API requests."""

    def __init__(self):
        self.subtask_service = SubtaskService(
            SubtaskRepository(),
            TaskRepository(),
            AuthServiceRepository(),
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, subtask_id):
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")

            current_app.logger.info(
                f"Fetching subtask with id {subtask_id} for {username}"
            )

            subtask = self.subtask_service.get_by_id(subtask_id)

            current_app.logger.info(f"Successfully fetched subtask for {username}.")
            if subtask:
                return jsonify(subtask)
            else:
                return jsonify(
                    {"message": "Subtask does not found!", "subtask_id": subtask_id}
                )

        except Exception as e:
            current_app.logger.error(f"Unexpected error: {str(e)}")
            return self.handle_error(f"Unexpected error: {str(e)}", 500)

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")
            data = request.get_json()

            new_subtask_id = self.subtask_service.insert(data)

            current_app.logger.info(
                f"Subtask with id '{new_subtask_id}' created successfully by {username}."  # noqa: E501
            )

            response = jsonify(
                {
                    "message": "New subtask successfully created!",
                    "subtask_id": new_subtask_id,
                }
            )
            response.status_code = 201

            return response

        except Exception as e:
            current_app.logger.error(f"Unexpected error: {str(e)}")
            return self.handle_error(f"Unexpected error: {str(e)}", 500)

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def patch(self):
        """Handles modifying a subtask."""
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")
            data = request.get_json()

            current_app.logger.info(
                f"Attempting to modify subtask with id '{data['id']}'"
            )

            modified_rows = self.subtask_service.modify(data)

            current_app.logger.info(
                f"Subtask successfully modified with id '{data['id']}', by {username}"
            )

            response = jsonify(
                {
                    "message": "Subtask successfully modified!",
                    "modified_rows": modified_rows,
                }
            )
            response.status_code = 204

            return response
        except Exception as e:
            return self.handle_error(f"Error modifying task: {str(e)}", 500)

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def delete(self, subtask_id):
        """Handles deleting a subtask."""
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")

            current_app.logger.info(
                f"Attempting to delete subtask with id '{subtask_id}'"
            )

            modified_rows = self.subtask_service.delete(subtask_id)

            current_app.logger.info(
                f"Subtask successfully deleted with id '{subtask_id}', by {username}"
            )

            response = jsonify(
                {
                    "message": "Subtask successfully deleted!",
                    "modified_rows": modified_rows,
                }
            )
            response.status_code = 204

            return response
        except Exception as e:
            return self.handle_error(f"Error deleting task: {str(e)}", 500)

    @staticmethod
    def handle_error(message, status_code):
        """Logs and returns a formatted error response."""
        current_app.logger.error(message)
        return {"error": message}, status_code
