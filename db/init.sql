CREATE DATABASE keycloak;

-- Switch to the target database (you must ensure this database exists)
USE msc_onlab;

-- Create the `users` table
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY, -- GUID for id
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    profile_picture VARCHAR(100),
    password VARCHAR(255) NOT NULL
);

-- Create the `households` table
CREATE TABLE households (
    id CHAR(36) PRIMARY KEY, -- GUID for id
    title VARCHAR(100) NOT NULL,
    creation_date DATETIME NOT NULL
);

-- Create the `household_people` table (many-to-many relationship between households and users)
CREATE TABLE household_people (
    household_id CHAR(36),
    user_id CHAR(36),
    PRIMARY KEY (household_id, user_id),
    FOREIGN KEY (household_id) REFERENCES households(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the `tasks` table
CREATE TABLE tasks (
    id CHAR(36) PRIMARY KEY, -- GUID for id
    title VARCHAR(100) NOT NULL,
    description TEXT,
    creation_date DATETIME NOT NULL,
    due_date DATE,
    done BOOLEAN NOT NULL,
    responsible_id CHAR(36),
    household_id CHAR(36),
    FOREIGN KEY (responsible_id) REFERENCES users(id),
    FOREIGN KEY (household_id) REFERENCES households(id)
);

-- Create the `subtasks` table
CREATE TABLE subtasks (
    id CHAR(36) PRIMARY KEY, -- GUID for id
    task_id CHAR(36) NOT NULL,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    done BOOLEAN NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);

-- Insert test data into `users`
INSERT INTO users (id, first_name, last_name, username, email, profile_picture, password) VALUES
    ('66f40d24-c53e-1eb2-d8bb-cd0d', 'Axel', 'Nemes', 'axel', 'axel.nemes@gmail.com', 'default', '9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61'),
    ('66f40ea5-c53e-1eb2-d8bb-cd1b', 'Alice', 'Anderson', 'alice', 'alice.anderson@gmail.com', 'woman_1', '9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61'),
    ('66f40ebd-c53e-1eb2-d8bb-cd1e', 'Bob', 'Brown', 'bob', 'bob.brown@gmail.com', 'man_2', '9098c53fe0e338cf9ef819e1048b6cf42e9d840425a43f96c64c19843ed80b61');

-- Insert test data into `households`
INSERT INTO households (id, title, creation_date) VALUES
    ('66f40fa9-5042-1e8e-cd6c-cdcf', 'House - Debrecen', '2024-09-25 13:27:05'),
    ('66f40fb0-5042-1e8e-cd6c-cdd2', 'Flat - Budapest', '2024-09-25 13:27:12'),
    ('66f40fb4-5042-1e8e-cd6c-cdd5', 'Flat - Szeged', '2024-09-25 13:27:16');

-- Insert test data into `household_people`
INSERT INTO household_people (household_id, user_id) VALUES
    ('66f40fa9-5042-1e8e-cd6c-cdcf', '66f40d24-c53e-1eb2-d8bb-cd0d'),
    ('66f40fa9-5042-1e8e-cd6c-cdcf', '66f40ea5-c53e-1eb2-d8bb-cd1b'),
    ('66f40fb0-5042-1e8e-cd6c-cdd2', '66f40d24-c53e-1eb2-d8bb-cd0d'),
    ('66f40fb0-5042-1e8e-cd6c-cdd2', '66f40ea5-c53e-1eb2-d8bb-cd1b'),
    ('66f40fb0-5042-1e8e-cd6c-cdd2', '66f40ebd-c53e-1eb2-d8bb-cd1e'),
    ('66f40fb4-5042-1e8e-cd6c-cdd5', '66f40d24-c53e-1eb2-d8bb-cd0d');

-- Insert test data into `tasks`
INSERT INTO tasks (id, title, description, creation_date, due_date, done, responsible_id, household_id) VALUES
    ('66f42ba1-5042-1e8e-cd6c-cdff', 'Wash the Dishes', 'Clean up the dishes after dinner', '2024-09-25 15:26:25', '2024-06-12', 0, '66f40ea5-c53e-1eb2-d8bb-cd1b', '66f40fa9-5042-1e8e-cd6c-cdcf'),
    ('66f42bb6-5042-1e8e-cd6c-ce08', 'Do the Laundry', 'Wash and dry the family clothes', '2024-09-25 15:26:46', '2024-06-13', 0, '66f40ea5-c53e-1eb2-d8bb-cd1b', '66f40fa9-5042-1e8e-cd6c-cdcf'),
    ('66f412e1-5042-1e8e-cd6c-cde4', 'Shopping', 'Buy groceries for the week', '2024-09-25 13:40:49', '2024-06-11', 0, '66f40d24-c53e-1eb2-d8bb-cd0d', '66f40fb0-5042-1e8e-cd6c-cdd2'),
    ('66f42909-5042-1e8e-cd6c-cde9', 'Mowing the lawn', 'Mow the lawn in the backyard', '2024-09-25 15:15:21', '2024-06-11', 0, '66f40ea5-c53e-1eb2-d8bb-cd1b', '66f40fb0-5042-1e8e-cd6c-cdd2'),
    ('66f4294c-5042-1e8e-cd6c-cdf1', 'Take animals for a walk', 'Walk the pets around the neighborhood', '2024-09-25 15:16:28', '2024-06-11', 0, '66f40ebd-c53e-1eb2-d8bb-cd1e', '66f40fb0-5042-1e8e-cd6c-cdd2');

-- Insert test data into `subtasks`
INSERT INTO subtasks (id, task_id, title, type, done) VALUES
    ('66f42bb6-5042-1e8e-cd6c-ce04', '66f42bb6-5042-1e8e-cd6c-ce08', 'Sort Clothes', 'checkbox', 0),
    ('66f42bb6-5042-1e8e-cd6c-ce05', '66f42bb6-5042-1e8e-cd6c-ce08', 'Wash Clothes', 'checkbox', 0);
-- Continue for all subtasks...
