-- init statuses table
-- depends: 20250316_03_aTwyC-add-cascade-deletion-for-team-user
CREATE TABLE IF NOT EXISTS `statuses` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `team_id` BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);
