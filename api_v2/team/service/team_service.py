from repository.team_repository import TeamRepository


class TeamService:
    def __init__(self, team_repositry: TeamRepository):
        self.team_repository = team_repositry

    def get_all(self):
        return self.team_repository.get_all()

    def insert(self, data):
        if not data["name"] or not data["description"]:
            raise Exception("Name and description are required")

        return self.team_repository.insert(data)
