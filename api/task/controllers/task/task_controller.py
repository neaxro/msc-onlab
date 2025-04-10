from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.auth_service_repository import AuthServiceRepository
from repository.task_repository import TaskRepository
from repository.team_repository import TeamRepository
from service.task_service import TaskService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class TaskController(Resource):
    """Controller for handling task-related API requests."""

    def __init__(self):
        self.task_service = TaskService(
            TaskRepository(), TeamRepository(), AuthServiceRepository()
        )

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self, task_id=None):
        """Handles GET requests for fetching tasks.

        - If `task_id` is provided, fetches a specific task.
        - If `teamId` is provided in query params, fetches tasks for that team.
        - Optionally filters by `assignedFor` (user ID).
        """
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")
            auth_header = request.headers.get("Authorization", None)

            if task_id:
                return self._fetch_task_by_id(task_id, username, auth_header)

            team_id = request.args.get("teamId")
            user_id = request.args.get("assignedFor")  # Can be None

            if not team_id:
                return self.handle_error("Missing teamId parameter!", 400)

            current_app.logger.info(f"Fetching tasks for {username} in team {team_id}")

            tasks = self.task_service.get_all(team_id, auth_header, user_id)

            current_app.logger.info(f"Successfully fetched tasks for {username}.")
            return jsonify(tasks)

        except Exception as e:
            current_app.logger.error(f"Unexpected error: {str(e)}")
            return self.handle_error(f"Unexpected error: {str(e)}", 500)

    def _fetch_task_by_id(self, task_id, username, auth_header):
        """Fetches a task by its ID."""
        try:
            task = self.task_service.get_by_id(task_id, auth_header)
            if not task:
                return self.handle_error(f"Task with ID {task_id} not found.", 404)

            current_app.logger.info(
                f"Successfully fetched task {task_id} for {username}."
            )
            return jsonify(task)

        except Exception as e:
            current_app.logger.error(f"Error fetching task: {str(e)}")
            return self.handle_error(f"Error fetching task {task_id}: {str(e)}", 500)

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

            new_task_id = self.task_service.insert(data, auth_header)

            current_app.logger.info(
                f"Task with id '{new_task_id}' created successfully by {username}."
            )

            response = jsonify(
                {"message": "New task successfully created!", "task_id": new_task_id}
            )
            response.status_code = 201

            return response
        except Exception as e:
            return self.handle_error(f"Error creating task: {str(e)}", 500)

    @staticmethod
    def handle_error(message, status_code):
        """Logs and returns a formatted error response."""
        current_app.logger.error(message)
        return {"error": message}, status_code

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def patch(self):
        """Handles modifying a task."""
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")

            data = request.get_json()

            current_app.logger.info(f"Attempting to modify task with id '{data['id']}'")

            modified_rows = self.task_service.modify(data)

            current_app.logger.info(
                f"Task successfully modified with id '{data['id']}', by {username}"
            )

            response = jsonify(
                {
                    "message": "Task successfully modified!",
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
    def delete(self, task_id):
        """Handles deleting a task."""
        try:
            user_data = get_decoded_token_from_request()
            username = user_data.get("preferred_username", "Unknown User")

            current_app.logger.info(f"Attempting to delete task with id '{task_id}'")

            modified_rows = self.task_service.delete(task_id)

            current_app.logger.info(
                f"Task successfully deleted with id '{task_id}', by {username}"
            )

            response = jsonify(
                {
                    "message": "Task successfully deleted!",
                    "modified_rows": modified_rows,
                }
            )
            response.status_code = 204

            return response
        except Exception as e:
            return self.handle_error(f"Error deleting task: {str(e)}", 500)
