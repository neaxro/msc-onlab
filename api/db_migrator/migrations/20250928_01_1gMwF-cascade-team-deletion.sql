-- cascade-team-deletion
-- depends: 20250504_01_crrnS-create-invitation-table
-- 1. Make statuses.team_id cascade
ALTER TABLE statuses
DROP FOREIGN KEY statuses_ibfk_1,
ADD CONSTRAINT fk_statuses_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE;

-- 2. Make tasks.team_id cascade
ALTER TABLE tasks
DROP FOREIGN KEY tasks_ibfk_2,
ADD CONSTRAINT fk_tasks_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE;

-- 3. Make tasks.status_id cascade
ALTER TABLE tasks
DROP FOREIGN KEY tasks_ibfk_1,
ADD CONSTRAINT fk_tasks_status FOREIGN KEY (status_id) REFERENCES statuses (id) ON DELETE CASCADE;
