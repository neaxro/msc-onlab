from repository.auth_service_repository import AuthServiceRepository
from repository.subtask_repository import SubtaskRepository
from repository.task_repository import TaskRepository


class SubtaskService:
    """Handles business logic for tasks."""

    def __init__(
        self,
        subtask_repositry: SubtaskRepository,
        task_repositry: TaskRepository,
        auth_repository: AuthServiceRepository,
    ):
        self.subtask_repositry = subtask_repositry
        self.task_repository = task_repositry
        self.auth_repository = auth_repository

    def get_all(self, task_id):
        return self.subtask_repositry.get_all(task_id)

    def get_by_id(self, subtask_id):
        return self.subtask_repositry.get_by_id(subtask_id)

    def insert(self, data):
        # Validate required fields
        required_fields = ["task_id", "title", "done"]
        for field in required_fields:
            if field not in data or data[field] is None:
                raise Exception(f"Missing required field: {field}")

        task_id = data["task_id"]
        title = data["title"]
        done = data["done"]

        # Check if task with task_id exists
        task = self.task_repository.get_by_id(task_id)
        if not task:
            raise Exception(f"Task does not exist with id: {task_id}")

        return self.subtask_repositry.insert(task_id=task_id, title=title, done=done)

    def modify(self, data):
        subtask_id = data.get("id", None)
        if not subtask_id:
            raise Exception("Subtask's id must be provided!")

        return self.subtask_repositry.modify(
            subtask_id=subtask_id,
            title=data.get("title", None),
            done=data.get("done", None),
        )

    def delete(self, subtask_id):
        return self.subtask_repositry.delete(subtask_id)
