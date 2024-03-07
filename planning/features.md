# Table of contents
[TOC]

# :page_facing_up: App Description
Cross platform applikáció (Android, iOS) amiben háztartással kapcsolatos feladatokat lehet létrehozni és emberekhez rendelni.
Bejelentkezés után létre lehet hozni / hozz lehet csatlakozni / ki lehet választani háztartást.
Háztartás kiválasztása után lehet saját/globális (összes) feladatokat megtekinteni. Feladatot lehet létrehozni, törölni, módosítani és felhasználót lehet hozzá társítani akinek a feladatot el kell végeznie.
Egy feladathoz több alfeladatot létre lehet hozni amikkel nyomon tudjuk követni, hogy az adott feladat milyen állapotban van. Amikor készen van egy feladat azt be lehet fejezni.

## :open_file_folder: Menü felépítése
- Háztartás kiválasztása
  - Háztartás részletes nézete
  - Létrehozása
  - Törlése
  - Módosítása
- Minden feladat
  - Új létrehozása
  - Feladat módosítása
  - Feladat törlése
  - Felhasználóra szűrés / mindenki
- Saját feladataim
  - Feladat befejezése
  - Feladat részletes megtekintése
    - Alfeladatok módosítása
- Régi feladatok
  - Felhasználóra szűrés
  - Feladat visszaállítása (újra aktív lesz, pl.: véletlen fejezte be)

# :pushpin: Features
- Bejelentkezés
- Regisztráció
- Feladat
  - megtekintése
    - rövid (listázáshoz)
    - részletes (módosításhoz)
  - létrehozása
  - törlése
  - módosítása
  - felhasználóhoz rendelése
  - alfeladat
    - létrehozása
    - törlése
    - módosítása (pl.: done)
- Háztartás
  - létrehozása
  - törlése
  - módosítása
  - megtekintése
  - felhasználó hozzáadása (pin kóddal)

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
| Get all (brief)            | GET   | /household                       | 200, 500    | :white_check_mark: |
| Get all (detailed)         | GET   | /household/detailed              | 200, 500    | :white_check_mark: |
| Get by id (detailed)       | GET   | /household/id/{household_id}        | 200, 500    | :white_check_mark: |
| Create                     | POST  | /household                       | 200, 500    | :white_check_mark: |
| Update                     | PATCH | /household/id/{household_id}        | 200, 500    | :white_check_mark: |
| Delete                     | DELETE| /household/id/{household_id}        | 200, 500    | :white_check_mark: |

## :love_letter: Invitation
> Response codes may change by the time

> Sends invitation email to the email address and the given url will redirects the use to the application.

| Detail                                | Mode  | URI                                    | HTTP Codes  | State |
|----------------------------|:-------:|----------------------------------|-------------|:----------:|
Adds the user to the household and redirects to the phone application | POST | /invite/<invitation_token> | 200, 500 | :construction:

## :date: Task
> Response codes may change by the time

| Detail                                | Mode  | URI                                    | HTTP Codes  | State |
|---------------------------------------|:-------:|----------------------------------------|-------------|:----:|
| Get all in household (brief)          | GET   | /household/{household_id}/task         | 200, 500    | :construction:
| Get all in household (detailed)       | GET   | /household/{household_id}/task/detailed| 200, 500    | :construction:
| Create                                | POST  | /household/{household_id}/task         | 200, 500    | :construction:
| Update                                | PATCH | /household/{household_id}/task/{task_id}| 200, 500    | :construction:
| Delete                                | DELETE| /household/{household_id}/task/{task_id}| 200, 500    | :construction:
| Assign user to task                   | PATCH | /household/{household_id}/task/{task_id}/assign/{user_id}| 200, 500    | :construction:
| Unassign user from task               | PATCH | /household/{household_id}/task/{task_id}/unassign          | 200, 500    | :construction:
| Get subtask                           | GET   | /task/{task_id}/subtask/{subtask_id}                       | 200, 500    | :construction:
| Add subtask to task                   | POST  | /task/{task_id}/subtask                                 | 200, 500    | :construction:
| Remove subtask from task              | DELETE| /task/{task_id}/subtask/{subtask_id}                       | 200, 500    | :construction:
| Update subtask                        | PATCH | /task/{task_id}/subtask/{subtask_id}                       | 200, 500    | :construction:
