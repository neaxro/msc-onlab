CREATE TABLE `teams` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL
);

CREATE TABLE `tasks` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `creation_date` DATETIME NOT NULL,
    `due_date` DATE NOT NULL,
    `status_id` BIGINT NOT NULL,
    `responsible_id` VARCHAR(255) NOT NULL,
    `team_id` BIGINT NOT NULL,
    FOREIGN KEY (`status_id`) REFERENCES `statuses`(`id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);

CREATE TABLE `subtasks` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `done` BOOLEAN NOT NULL,
    `task_id` BIGINT NOT NULL,
    FOREIGN KEY (`task_id`) REFERENCES `tasks`(`id`)
);

CREATE TABLE `statuses` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `team_id` BIGINT NOT NULL,
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);

CREATE TABLE `team_user` (
    `team_id` BIGINT NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`team_id`, `user_id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);
