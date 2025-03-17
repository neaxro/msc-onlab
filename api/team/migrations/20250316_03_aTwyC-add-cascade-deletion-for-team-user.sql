-- Add cascade deletion for team_user
-- depends: 20250316_02_SChXM-create-team-user-table-if-not-exists
ALTER TABLE team_user
ADD CONSTRAINT fk_team_user_team
FOREIGN KEY (team_id) REFERENCES teams(id)
ON DELETE CASCADE;
