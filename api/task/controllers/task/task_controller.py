from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.task_repository import TaskRepository
from service.task_service import TaskService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import get_decoded_token_from_request, requires_auth


class TaskController(Resource):
    def __init__(self):
        self.task_service = TaskService(task_repositry=TaskRepository())

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self):
        try:
            user_data = get_decoded_token_from_request()

            team_id = request.args.get("teamId")
            user_id = request.args.get("assignedFor", None)

            if not team_id:
                raise Exception("Missing teamId parameter!")

            current_app.logger.info(
                f"Getting tasks for {user_data['preferred_username']} user within team {team_id}"  # noqa: E501
            )

            try:
                result = self.task_service.get_all(team_id, user_id)

                current_app.logger.info(
                    f"Tasks successfully fetched for {user_data['preferred_username']}!"
                )
                return jsonify(result)

            except Exception as e:
                current_app.logger.info(
                    f"Error occured during fetching tasks for user {user_data['preferred_username']}."  # noqa: E501
                )
                return {
                    "error": "Error occured during fetching tasks for user.",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
