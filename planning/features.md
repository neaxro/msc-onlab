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
| Get User by ID  | GET | /users/{user_id} | 200, 404, 500 |	Get user details by ID. | :white_check_mark:
| Modify User by ID  | PATCH | /users/{user_id} | 200, 400, 404, 500 |	Modify user details by ID. | :white_check_mark:
| Disable User by ID  | DELETE | /users/{user_id} | 204, 404, 500 |	Disable user, so login is restricted. | :white_check_mark:

## :office: Team service
> Response codes may change by the time

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Get all teams| GET| /teams | 200, 500| List all teams a user belongs to. Get username from token. | :white_check_mark: |
| Get team by ID| GET| /teams/{team_id} | 200, 500| Get team details by ID. Get username from token. | :white_check_mark: |
| Create team| POST| /teams | 201, 400, 500| Create a new team (requires name and description). | :white_check_mark: |
| Modify team| PATCH| /teams/{team_id} | 200, 500| Modify a team's name or description. | :white_check_mark: |
| Delete team| DELETE| /teams/{team_id} | 200, 500| Delete a team. | :white_check_mark: |
| Add user to team| POST| /membership/{team_id}/invited/{invited_user_id} | 200, 500| Adds user to team. | :white_check_mark: |
| Removes user from team| DELETE| /membership/{team_id}/invited/{invited_user_id} | 200, 500| Removes user from team. | :white_check_mark: |
| Get users of team| GET | /membership/{team_id}/members | 200, 500| Get all user's data from team | :white_check_mark: |
| Get teams where user is member | GET | /membership/{user_id}/teams | 200, 500| Get all teams data where user is member | :white_check_mark: |
| Team info | GET | /teams/{team_id}/info | 200, 500| Get team's details, such as task statuses and its ids... | :white_check_mark: |

## :date: Task service

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| Get all tasks in team (brief)| GET| /tasks?teamId={teamId}?assignedFor={userId} | 200, 500| List tasks for a specific team. | :white_check_mark: |
| Get task by ID| GET| /tasks/{task_id} | 200, 500| List tasks for a specific team. Include subtasks | :white_check_mark: |
| Create task| POST| /tasks?forTeam={teamId} | 201, 500| List tasks for a specific team. | :white_check_mark: |
| Modify task| PATCH| /tasks/{task_id} | 204, 500| Modify task details (title, description, status, etc.) | :white_check_mark: |
| Delete task| DELETE| /tasks/{task_id} | 204, 500| Delete a task. | :white_check_mark: |
| Create subtask| POST| /tasks/{task_id}/subtasks | 201, 500| Create a new subtask for a task. | :white_check_mark: |
| Modify subtask| PATCH| /subtasks/{subtask_id} | 200, 500| Modify subtask details (title, status, etc.). | :white_check_mark: |
| Delete subtask| DELETE| /subtasks/{subtask_id} | 200, 500| Delete a subtask. | :white_check_mark: |

## :love_letter: Invitation

| Detail          | Mode | URI        | HTTP Codes  | Description  | State |
|-----------------|:----:|------------|-------------|-------------|:-----:|
| List available invitations| GET| /invitations | 200, 500| Creates an invitation | :white_check_mark: |
| Invite user| POST| /invitations | 204, 500| Creates an invitation | :white_check_mark: |
| Accept/Decline invitation| PATCH| /invitations?decision{accept/decline}?token={token} | 200, 500| Accepts invitation so user will be part of team or declines it. | :construction: |
