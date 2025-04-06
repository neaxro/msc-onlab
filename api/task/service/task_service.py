from repository.task_repository import TaskRepository
from repository.team_repository import TeamRepository


class TaskService:
    """Handles business logic for tasks."""

    def __init__(self, task_repositry: TaskRepository, team_repository: TeamRepository):
        self.task_repository = task_repositry
        self.team_repository = team_repository

    # TODO: Include user data not just assigned_id
    def get_all(self, team_id, user_id=None):
        """Fetches all tasks for a given team, optionally filtering by user."""
        return self.task_repository.get_all(team_id, user_id)

    def get_by_id(self, task_id):
        """Fetches a specific task by ID."""
        return self.task_repository.get_by_id(task_id)

    def insert(
        self, title, description, due_date, responsible_id, team_id, auth_header
    ):
        """Creates a new task and returns the created task."""

        try:
            team_data = self.team_repository.get_teams_info(team_id, auth_header)

            todo_status_code = next(
                status for status in team_data["statuses"] if status["name"] == "TODO"
            )["id"]

            return self.task_repository.insert(
                title=title,
                description=description,
                due_date=due_date,
                responsible_id=responsible_id,
                team_id=team_id,
                status_id=todo_status_code,
            )
        except Exception as e:
            raise e
