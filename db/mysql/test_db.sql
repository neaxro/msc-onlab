
USE `msc_onlab`;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS `subtasks`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `statuses`;
DROP TABLE IF EXISTS `team_user`;
DROP TABLE IF EXISTS `teams`;

-- Create `teams` table
CREATE TABLE IF NOT EXISTS `teams` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    PRIMARY KEY (`id`)
);

-- Create `team_user` table
CREATE TABLE IF NOT EXISTS `team_user` (
    `team_id` BIGINT UNSIGNED NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`team_id`, `user_id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);

-- Add foreign key constraint for `team_user`
ALTER TABLE `team_user`
    ADD CONSTRAINT fk_team_user_team
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
    ON DELETE CASCADE;

-- Create `statuses` table
CREATE TABLE IF NOT EXISTS `statuses` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `team_id` BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (`team_id`) REFERENCES `teams`(`id`)
);

-- Create `tasks` table
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

-- Create `subtasks` table
CREATE TABLE IF NOT EXISTS `subtasks` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `done` BOOLEAN NOT NULL,
    `task_id` BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (`task_id`) REFERENCES `tasks`(`id`)
);

-- Insert test data into `teams` table
INSERT INTO `teams` (`name`, `description`) VALUES
('Development', 'Team responsible for software development'),
('Marketing', 'Team responsible for marketing campaigns'),
('Design', 'Team responsible for UI/UX design');

-- Insert test data into `statuses` table
INSERT INTO `statuses` (`name`, `team_id`) VALUES
('To Do', 1),  -- Status for Development team
('In Progress', 1),  -- Status for Development team
('Completed', 1),  -- Status for Development team
('To Do', 2),  -- Status for Marketing team
('In Progress', 2),  -- Status for Marketing team
('Completed', 2),  -- Status for Marketing team
('To Do', 3),  -- Status for Design team
('In Progress', 3),  -- Status for Design team
('Completed', 3);  -- Status for Design team

-- Insert test data into `tasks` table
INSERT INTO `tasks` (`title`, `description`, `creation_date`, `due_date`, `status_id`, `responsible_id`, `team_id`) VALUES
('Develop new feature', 'Develop a new feature for the product', NOW(), '2025-04-10', 2, 'b0a62cbd-a24f-40cd-8a37-44f934abcbf8', 1),
('Create marketing plan', 'Plan the next marketing campaign', NOW(), '2025-04-15', 5, 'fcb453a0-6a8a-4ebc-a898-74e7c10acc64', 2),
('Redesign homepage', 'Update homepage layout', NOW(), '2025-04-20', 8, '87faba81-8d4b-40f8-8aa3-044d3838c4d8', 3);

-- Insert test data into `subtasks` table
INSERT INTO `subtasks` (`title`, `done`, `task_id`) VALUES
('Set up development environment', false, 1),
('Write unit tests', false, 1),
('Write campaign email', false, 2),
('Design homepage layout', false, 3);

-- Insert test
