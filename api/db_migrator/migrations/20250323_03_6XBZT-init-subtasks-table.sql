-- init subtasks table
-- depends: 20250323_02_l0WKO-init-tasks-table
CREATE TABLE IF NOT EXISTS `subtasks` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `done` BOOLEAN NOT NULL,
    `task_id` BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (`task_id`) REFERENCES `tasks`(`id`)
);
