-- Create team_user table if not exists
-- depends: 20250316_01_8oISw-create-teams-table-if-not-exists
CREATE TABLE IF NOT EXISTS `team_user` (
    `team_id` BIGINT NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`team_id`, `user_id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);
