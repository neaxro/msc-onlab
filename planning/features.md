# Table of contents
[TOC]

# :page_facing_up: App Description
Cross-platform application (Android, iOS) for creating and assigning household tasks. After logging in, you can create/join/select a household. Once a household is selected, you can view your own/global (all) tasks within a household. You can create, delete, modify tasks and associate users to tasks. Subtasks can be created for a task to track its progress. When a task is completed, it can be marked as done.

## :open_file_folder: Menu Structure
- Select Household
  - Detailed View of Household
  - Create
  - Delete
  - Modify
- All Tasks
  - Create New
  - Modify Task
  - Delete Task
  - Filter by User / All Users
- My Tasks
  - Complete Task
  - View Task Details
    - Modify Subtasks
- Old Tasks
  - Filter by User
  - Restore Task (set as active again, e.g., accidentally marked as done)

# :pushpin: Features
- Login
- Registration
- Task
  - View
    - Short (for listing)
    - Detailed (for modification)
  - Create
  - Delete
  - Modify
  - Assign to user
  - Subtask
    - Create
    - Delete
    - Modify (e.g., done)
- Household
  - Create
  - Delete
  - Modify
  - View
  - Add User (with url/email)

# :chart_with_upwards_trend: API endpoints

## :door: Login
> Response codes may change by the time

| Detail          | Mode | URI        | HTTP Codes  | State |
|-----------------|:----:|------------|-------------|:-----:|
| Login           | PUT  | /login     | 200, 500    | :white_check_mark:
| Register        | PUT  | /register  | 200, 500    | :white_check_mark:

## :house: Household
> Response codes may change by the time

| Detail                     | Mode  | URI                              | HTTP Codes  | State |
|----------------------------|:-------:|----------------------------------|-------------|:----------:|
| Get all (brief)            | GET   | /household/all                       | 200, 500    | :white_check_mark: |
| Get all (detailed)         | GET   | /household/all/detailed              | 200, 500    | :white_check_mark: |
| Get by id (detailed)       | GET   | /household/id/{household_id}/detailed        | 200, 500    | :white_check_mark: |
| Get users in household     | GET   | /household/id/{household_id}/users        | 200, 500    | :white_check_mark: |
| Create                     | POST  | /household                       | 200, 500    | :white_check_mark: |
| Update                     | PATCH | /household/id/{household_id}        | 200, 500    | :white_check_mark: |
| Delete                     | DELETE| /household/id/{household_id}        | 200, 500    | :white_check_mark: |

### :love_letter: Invitation
> Response codes may change by the time

> Sends invitation email to the email address and the given url will redirects the use to the application.

| Detail                                | Mode  | URI                                    | HTTP Codes  | State |
|----------------------------|:-------:|----------------------------------|-------------|:----------:|
Sends invitations to the invited users via email | POST | /household/invite/ | 200, 500 | :white_check_mark:
Adds the user to the token's specified household | POST | /household/accept-invite/<invitation_token> | 200, 500 | :white_check_mark:

### :date: Task
> Response codes may change by the time

| Detail                                | Mode  | URI                                    | HTTP Codes  | State |
|---------------------------------------|:-------:|----------------------------------------|-------------|:----:|
| Get all in household (brief)          | GET   | /household/id/{household_id}/tasks         | 200, 500    | :white_check_mark:
| Get all in household (detailed)       | GET   | /household/id/{household_id}/tasks/all/detailed| 200, 500    | :white_check_mark:
| Create                                | POST  | /household/id/{household_id}/tasks         | 200, 500    | :white_check_mark:
| Update                                | PATCH | /household/id/{household_id}/tasks/id/{task_id}| 200, 500    | :white_check_mark:
| Delete                                | DELETE| /household/id/{household_id}/tasks/id/{task_id}| 200, 500    | :white_check_mark:
| Assign user to task                   | PATCH | /tasks/id/{task_id}/assign-to/{user_id}| 200, 500    | :white_check_mark:
| Unassign user from task               | PATCH | /tasks/id/{task_id}/unassign-user         | 200, 500    | :white_check_mark:

#### :information_source: Subtask
> Response codes may change by the time

| Detail                                | Mode  | URI                                    | HTTP Codes  | State |
|---------------------------------------|:-------:|----------------------------------------|-------------|:----:|
| Get subtask                           | GET   | /subtask/{subtask_id}                       | 200, 500    | :construction:
| Add subtask to task                   | POST  | /subtask/add-to/{task_id}                                 | 200, 500    | :construction:
| Remove subtask from task              | DELETE| /subtask/{subtask_id}/remove-from/{task_id}                       | 200, 500    | :construction:
| Update subtask                        | PATCH | /subtask/{subtask_id}                       | 200, 500    | :construction: