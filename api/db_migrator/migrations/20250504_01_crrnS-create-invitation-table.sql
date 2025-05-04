-- create invitation table
-- depends: 20250323_03_6XBZT-init-subtasks-table
CREATE TABLE IF NOT EXISTS `invitations` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT UNSIGNED NOT NULL,
    `invited_user_id` VARCHAR(255) NOT NULL,
    `inviter_user_id` VARCHAR(255) NOT NULL,
    `token` VARCHAR(64) NOT NULL UNIQUE,
    `email` VARCHAR(255) NOT NULL,
    `expires` DATETIME NOT NULL,
    `accepted` DATETIME,
    `created_at` DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`) ON DELETE CASCADE
);
