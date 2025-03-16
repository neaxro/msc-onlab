# Table of contents
[TOC]

# :page_facing_up: App Description
Android application for creating and assigning teams tasks. After logging in, you can create/join/select a team. Once a team is selected, you can view your own/global (all) tasks within a team. You can create, delete, modify tasks and associate users to tasks. Subtasks can be created for a task to track its progress. When a task is completed, it can be marked as done.

## :open_file_folder: Menu Structure, use cases
- Select Team
  - List of teams where user belongs
  - Create (name, description)
  - Delete
  - Modify (name, description)

- All Tasks within selected team
  - Create New (title, description, due_date, status, responsible, team, subtasks)
  - Modify Task (title, description, due_date, status, responsible, team, subtasks)
  - Delete Task
  - Filters:
    - by user
    - only not DONE statuses

- My Tasks
  - Complete Task
  - View Task Details
    - Modify Subtasks
- Old Tasks
  - Filter by User
  - Restore Task (set as active again, e.g., accidentally marked as done)

# DB schema and must supported actions

- Team
  - Create
  - List (filters: only where user belongs!)
  - Get by id
  - Modify
  - Delete

- Tasks
  - Create
  - List (filters: by user, not DONE tasks only)
  - Get task by id
  - Modify
  - Delete

- Subtasks:
  - Create (for task)
  - List (for task only)
  - Get subtask by id
  - Modify
  - Delete (define constraint!)

- Statuses:
  - List for selected team (TODO, IN_PROGRESS, BLOCKED, IN_REVIEW, DONE, CLOSED)
  - Create
  - Modify
  - Delete (define constraint!)

- Users:
  - Get current user by id
  - List users within team
  - Modify user
  - Delete user

# :pushpin: Features
- Auth
  - Login
  - Registration
- Team
  - Create
  - Delete
  - Modify
  - View details
  - Add User (with url/email)

- Task
  - View
    - In list
    - Detailed (for modification)
  - Create
  - Delete
  - Modify
  - Assign to user

- Subtask
  - Create
  - Delete
  - Modify (e.g., done)
  - List for selected task

- Statuses:
  - List statuses within a team
  - Edit
  - Create
  - Delete

## Microservices

- Auth:
  - features: Auth, Users

- Team:
  - features: Team

- Task:
  - features: Task, Subtask

- Status:
  - features: Statuses

# :chart_with_upwards_trend: API endpoints

## :door: Auth service
> Response codes may change by the time

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Login           | POST  | /login     | 200, 401, 500    | Login a user with username/password. | :white_check_mark:
| Register        | POST  | /register  | 200, 401, 500    | Register a new user. | :white_check_mark:
| Get User by ID  | GET | /users/{user_id} | 200, 404, 500 |	Get user details by ID. | :construction:
| Modify User by ID  | PATCH | /users/{user_id} | 200, 400, 404, 500 |	Modify user details by ID. | :construction:
| Delete User by ID  | DELETE | /users/{user_id} | 204, 404, 500 |	Delete user details by ID. | :construction:

## :office: Team service
> Response codes may change by the time

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Get all teams| GET| /teams | 200, 500| List all teams a user belongs to. Get username from token. | :construction: |
| Get team by ID| GET| /teams/{team_id} | 200, 500| Get team details by ID. Get username from token. | :construction: |
| Create team| POST| /teams | 201, 400, 500| Create a new team (requires name and description). | :construction: |
| Modify team| PATCH| /teams/{team_id} | 200, 500| Modify a team's name or description. | :construction: |
| Modify team| DELETE| /teams/{team_id} | 200, 500| Delete a team. | :construction: |

## :date: Task service

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Get all tasks in team (brief)| GET| /tasks?teamId={teamId}?assignedFor={userId} | 200, 500| List tasks for a specific team. | :construction: |
| Get task by ID| GET| /tasks/{task_id} | 200, 500| List tasks for a specific team. Include subtasks | :construction: |
| Create task| POST| /tasks?forTeam={teamId} | 201, 500| List tasks for a specific team. | :construction: |
| Modify task| PATCH| /tasks/{task_id} | 200, 500| Modify task details (title, description, status, etc.) | :construction: |
| Delete task| DELETE| /tasks/{task_id} | 200, 500| Delete a task. | :construction: |
| Assign user to task| PATCH| /tasks/{task_id}/assign/{user_id} | 200, 500| Assign a user to a task. | :construction: |
| Unassign user from task| PATCH| /tasks/{task_id}/unassign/{user_id} | 200, 500| Unassign a user from a task. | :construction: |
| Unassign user from task| PATCH| /tasks/{task_id}/unassign/{user_id} | 200, 500| Unassign a user from a task. | :construction: |
| Create subtask| POST| /tasks/{task_id}/subtasks | 201, 500| Create a new subtask for a task. | :construction: |
| Modify subtask| PATCH| /subtasks/{subtask_id} | 200, 500| Modify subtask details (title, status, etc.). | :construction: |
| Delete subtask| DELETE| /subtasks/{subtask_id} | 200, 500| Delete a subtask. | :construction: |

## :chart_with_upwards_trend: Status service
| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Get all statuses for team| GET| /statuses?forTeam={teamId}| 200, 500| Get all statuses for a team. | :construction: |
| Create new status| POST| /statuses?forTeam={teamId}| 201, 500| Create a new status for a team. | :construction: |
| Modify status| PATCH| /statuses/{status_id}| 200, 500| Modify an existing status. | :construction: |
| Delete status| DELETE| /statuses/{status_id}| 200, 500| Delete a status from a team. | :construction: |


## :love_letter: Invitation
> Response codes may change by the time

> Sends invitation email to the email address and the given url will redirects the use to the application.

Todo...
