from repository.auth_service_repository import AuthServiceRepository
from repository.task_repository import TaskRepository
from repository.team_repository import TeamRepository


class TaskService:
    """Handles business logic for tasks."""

    def __init__(
        self,
        task_repositry: TaskRepository,
        team_repository: TeamRepository,
        auth_repository: AuthServiceRepository,
    ):
        self.task_repository = task_repositry
        self.team_repository = team_repository
        self.auth_repository = auth_repository

    def _get_responsible_data_from_task(self, task, auth_header):
        responsible_id = task["responsible_id"]
        return self.auth_repository.get_user_by_id(responsible_id, auth_header)

    def _replace_user_id_to_data(self, task, auth_header):
        responsible_data = self._get_responsible_data_from_task(task, auth_header)
        task.pop("responsible_id")
        task["responsible"] = responsible_data

    def get_all(self, team_id, auth_header, user_id=None):
        """Fetches all tasks for a given team, optionally filtering by user."""
        tasks = self.task_repository.get_all(team_id, user_id)

        for task in tasks:
            self._replace_user_id_to_data(task, auth_header)

        return tasks

    def get_by_id(self, task_id, auth_header):
        """Fetches a specific task by ID."""
        task = self.task_repository.get_by_id(task_id)
        self._replace_user_id_to_data(task, auth_header)

        return task

    def insert(self, data, auth_header):
        """Creates a new task and returns the created task."""

        # Validate required fields
        required_fields = ["title", "description", "due_date", "team_id"]
        for field in required_fields:
            if field not in data or data[field] is None:
                raise Exception(f"Missing required field: {field}")

        # Convert due_date if necessary
        due_date = data.get("due_date")
        if not isinstance(due_date, str):  # Ensure it's a string
            raise Exception("Invalid due_date format. Expected 'YYYY-MM-DD'")

        title = data["title"]
        description = data["description"]
        responsible_id = data.get("responsible_id")  # Nullable field
        team_id = data["team_id"]

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

    def modify(self, task_data):
        """Updates the task attributes which are not None. Other values are kept as they are."""  # noqa: E501

        task_id = task_data.get("id", None)
        if not task_id:
            raise Exception("Task's id must be provided!")

        return self.task_repository.modify(
            task_id=task_id,
            title=task_data.get("title", None),
            description=task_data.get("description", None),
            due_date=task_data.get("due_date", None),
            status_id=task_data.get("status_id", None),
            responsible_id=task_data.get("responsible_id", None),
        )
