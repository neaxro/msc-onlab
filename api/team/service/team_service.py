from repository.team_repository import TeamRepository
from repository.team_user_repository import TeamUserRepository


class TeamService:
    def __init__(
        self, team_repositry: TeamRepository, team_user_repository: TeamUserRepository
    ):
        self.team_repository = team_repositry
        self.team_user_repository = team_user_repository

    def get_all(self, user_data):
        user_id = user_data["sub"]
        return self.team_repository.get_all(user_id)

    def get_by_id(self, team_id, user_data):
        user_id = user_data["sub"]
        return self.team_repository.get_by_id(team_id, user_id)

    def __get_by_name(self, team_name):
        return self.team_repository.get_by_name(team_name)

    def insert(self, data, user_data):
        # Check data before creating anything
        if not data["name"] or not data["description"]:
            raise Exception("Name and description are required")

        # Check for existing team
        existing_team = self.__get_by_name(data["name"])
        if existing_team is not None:
            raise Exception(f"Team with \"{data['name']}\" name already exists!")

        # Here everything is okay, new team can be created
        user_id = user_data["sub"]
        new_team_id = self.team_repository.insert(data)

        if new_team_id is not None:
            try:
                new_team_user_id = self.team_user_repository.insert(
                    new_team_id, user_id
                )
                print(f"New team user id: {new_team_user_id}")

                if new_team_user_id is None:
                    # Attempt to delete the team if the insertion fails
                    deleted_record_count = self.team_repository.delete(new_team_id)
                    if deleted_record_count > 0:
                        raise Exception(
                            "Failed to create new team and successfully deleted the created team!"  # noqa: E501
                        )
                    raise Exception(
                        "Error occurred while creating the team. Insertion failed."
                    )

                return new_team_id

            except Exception as e:
                # Log the error and raise with an informative message
                print(f"Error during team creation process: {str(e)}")
                raise Exception(
                    f"An error occurred while processing the team creation: {str(e)}"
                )

    def update(self, data, user_data):
        user_id = user_data["sub"]
        team_membership = self.team_user_repository.is_user_part_of_team(
            data["id"], user_id
        )

        # User does not belongs to team
        if team_membership is None:
            raise Exception(
                f"User {user_data['preferred_username']} is not part of team with id {data['id']}!"  # noqa: E501
            )

        return self.team_repository.update(data, user_id)

    def delete(self, team_id, user_data):
        user_id = user_data["sub"]
        team_membership = self.team_user_repository.is_user_part_of_team(
            team_id, user_id
        )

        # User does not belongs to team
        if team_membership is None:
            raise Exception(
                f"User {user_data['preferred_username']} is not part of team with id {team_id}!"  # noqa: E501
            )

        return self.team_repository.delete(team_id)
