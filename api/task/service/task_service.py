from repository.task_repository import TaskRepository


class TaskService:
    def __init__(
        self,
        task_repositry: TaskRepository,
    ):
        self.task_repositry = task_repositry

    # TODO: Include user data not just assigned_id
    def get_all(self, team_id, user_id=None):
        return self.task_repositry.get_all(team_id, user_id)
