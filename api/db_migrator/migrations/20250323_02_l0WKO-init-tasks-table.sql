-- init tasks table
-- depends: 20250323_01_WRLhG-init-statuses-table
CREATE TABLE IF NOT EXISTS `tasks` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `creation_date` DATETIME NOT NULL,
    `due_date` DATE NOT NULL,
    `status_id` BIGINT UNSIGNED NOT NULL,
    `responsible_id` VARCHAR(255) NOT NULL,
    `team_id` BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (`status_id`) REFERENCES `statuses`(`id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);
