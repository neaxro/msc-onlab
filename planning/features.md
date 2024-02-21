# Table of contents
[TOC]

# App Description
Cross platform applikáció (Android, iOS) amiben háztartással kapcsolatos feladatokat lehet létrehozni és emberekhez rendelni.
Bejelentkezés után létre lehet hozni / hozz lehet csatlakozni / ki lehet választani háztartást.
Háztartás kiválasztása után lehet saját/globális (összes) feladatokat megtekinteni. Feladatot lehet létrehozni, törölni, módosítani és felhasználót lehet hozzá társítani akinek a feladatot el kell végeznie.
Egy feladathoz több alfeladatot létre lehet hozni amikkel nyomon tudjuk követni, hogy az adott feladat milyen állapotban van. Amikor készen van egy feladat azt be lehet fejezni.

## Menü felépítése
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

# Features
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

# API endpoints

## Login
> Response codes may change by the time

| Detail          | Mode | URI        | HTTP Codes  |
|-----------------|------|------------|-------------|
| Login           | PUT  | /login     | 200, 400    |
| Register        | PUT  | /register  | 200, 400    |

## Household
> Response codes may change by the time

| Detail                     | Mode  | URI                              | HTTP Codes  |
|----------------------------|-------|----------------------------------|-------------|
| Get all (brief)            | GET   | /household                       | 200, 400    |
| Get all (detailed)         | GET   | /household/detailed              | 200, 400    |
| Get by id (detailed)       | GET   | /household/{household_id}        | 200, 400    |
| Create                     | POST  | /household                       | 200, 400    |
| Update                     | PATCH | /household/{household_id}        | 200, 400    |
| Delete                     | DELETE| /household/{household_id}        | 200, 400    |

## Task
> Response codes may change by the time

| Detail                                | Mode  | URI                                    | HTTP Codes  |
|---------------------------------------|-------|----------------------------------------|-------------|
| Get all in household (brief)          | GET   | /household/{household_id}/task         | 200, 400    |
| Get all in household (detailed)       | GET   | /household/{household_id}/task/detailed| 200, 400    |
| Create                                | POST  | /household/{household_id}/task         | 200, 400    |
| Update                                | PATCH | /household/{household_id}/task/{task_id}| 200, 400    |
| Delete                                | DELETE| /household/{household_id}/task/{task_id}| 200, 400    |
| Assign user to task                   | PATCH | /household/{household_id}/task/{task_id}/assign/{user_id}| 200, 400    |
| Unassign user from task               | PATCH | /household/{household_id}/task/{task_id}/unassign          | 200, 400    |
| Get subtask                           | GET   | /task/{task_id}/subtask/{subtask_id}                       | 200, 400    |
| Add subtask to task                   | POST  | /task/{task_id}/subtask                                 | 200, 400    |
| Remove subtask from task              | DELETE| /task/{task_id}/subtask/{subtask_id}                       | 200, 400    |
| Update subtask                        | PATCH | /task/{task_id}/subtask/{subtask_id}                       | 200, 400    |
