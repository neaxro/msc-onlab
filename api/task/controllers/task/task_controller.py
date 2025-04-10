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

            data = request.get_json()

            # Validate required fields
            required_fields = ["title", "description", "due_date", "team_id"]
            for field in required_fields:
                if field not in data or data[field] is None:
                    return self.handle_error(f"Missing required field: {field}", 400)

            # Convert due_date if necessary
            due_date = data.get("due_date")
            if not isinstance(due_date, str):  # Ensure it's a string
                return self.handle_error(
                    "Invalid due_date format. Expected 'YYYY-MM-DD'", 400
                )

            # Get token for further api call
            auth_header = request.headers.get("Authorization", None)

            new_task_id = self.task_service.insert(
                title=data["title"],
                description=data["description"],
                due_date=due_date,
                responsible_id=data.get("responsible_id"),  # Nullable field
                team_id=data["team_id"],
                auth_header=auth_header,
            )

            current_app.logger.info(
                f"Task with id '{new_task_id}' created successfully by {username}."
            )

            response = jsonify(
                {"message": "New task successfully created!", "task_id": new_task_id}
            )
            response.status_code = 201

            return response
        except Exception as e:
            current_app.logger.error(f"Error creating task: {str(e)}")
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
            current_app.logger.error(f"Error modifying task: {str(e)}")
            return self.handle_error(f"Error modifying task: {str(e)}", 500)
