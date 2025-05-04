from repository.auth_service_repository import AuthServiceRepository
from repository.subtask_repository import SubtaskRepository


class SubtaskService:
    """Handles business logic for tasks."""

    def __init__(
        self,
        subtask_repositry: SubtaskRepository,
        auth_repository: AuthServiceRepository,
    ):
        self.subtask_repositry = subtask_repositry
        self.auth_repository = auth_repository

    def get_all(self, task_id):
        return self.subtask_repositry.get_all(task_id)
