from repository.task_repository import TaskRepository


class TaskService:
    """Handles business logic for tasks."""

    def __init__(self, task_repositry: TaskRepository):
        self.task_repository = task_repositry

    # TODO: Include user data not just assigned_id
    def get_all(self, team_id, user_id=None):
        """Fetches all tasks for a given team, optionally filtering by user."""
        return self.task_repository.get_all(team_id, user_id)

    def get_by_id(self, task_id):
        """Fetches a specific task by ID."""
        return self.task_repository.get_by_id(task_id)

    # TODO: Get the team's "TODO" status' id
    def insert(self, title, description, due_date, responsible_id, team_id):
        """Creates a new task and returns the created task."""
        return self.task_repository.insert(
            title=title,
            description=description,
            due_date=due_date,
            responsible_id=responsible_id,
            team_id=team_id,
            status_id=1,
        )
