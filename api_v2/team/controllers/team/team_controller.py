from flask import current_app, jsonify, request
from flask_restful import Resource
from repository.team_repository import TeamRepository
from service.team_service import TeamService
from utils.metrics import count_requests, latency_request, time_request
from utils.token_check import requires_auth


class Team(Resource):
    def __init__(self):
        self.team_service = TeamService(TeamRepository())

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def get(self):
        try:
            current_app.logger.info("Getting all teams...")

            try:
                response = self.team_service.get_all()
                current_app.logger.info("Successfuly fetched all team!")
                return jsonify(response)

            except Exception as e:
                current_app.logger.info("Error occured during fetching all team.")
                return {
                    "error": "Error occured during fetching all team.",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500

    @requires_auth
    @count_requests
    @time_request
    @latency_request
    def post(self):
        try:
            body = request.get_json()
            data = {"name": body.get("name"), "description": body.get("description")}

            current_app.logger.info(f"Creating new team with name \"{data['name']}\"")

            try:
                response = self.team_service.insert(data)
                current_app.logger.info(
                    f"New team called \"{data['name']}\" successfuly created!"
                )
                return jsonify(
                    {"message": "New team successfuly created!", "team_id": response}
                )
            except Exception as e:
                current_app.logger.info(
                    f"Error occured during \"{data['name']}\" team creation."
                )
                return {
                    "error": "Error occured during team creation",
                    "details": str(e),
                }, 401

        except Exception as e:
            current_app.logger.info(f"Something went wrong. Error: {str(e)}")
            return {"error": "Something went wrong", "details": str(e)}, 500
