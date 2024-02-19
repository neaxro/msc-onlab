# App Description
Cross platform applikáció (Android, iOS) amiben háztartással kapcsolatos feladatokat lehet létrehozni és emberekhez rendelni.

Bejelentkezés után ki lehet választani háztartást. Kiválasztás után lehet megnézni feladatokat, létrehozni feladatokat és módosítani feladatokat.

## Menü felépítése
- Háztartás kiválasztása
  - Háztartás részletes nézete
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
  - Feladat visszaállítása (újra aktív lesz, véletlen fejezte be)

Feladatot létre lehet hozni bárki számára. Létrehozáskor az alábbi adatokat kell szolgáltatni:
- Feladat címe
- Leírása
- Határideje
- Alfeladatok (az összes alfeladat befejezése a task befehezését jelenti, opcionális)
- Tulajdonosa (akihez rendelték a feladatot, neki kell elvégeznie), opcionális leget Unassigned

## Feladatok megtekintése

# Features
- Bejelentkezés
- Regisztráció
- Feladat
  - megtekintése
    - rövid (listázáshoz)
    - részletes (módosításhoz)
  - léterhozása
  - törlése
  - módosítása
  - felhasználóhoz rendelése
  - alfeladat módosítása (pl.: kipipálása)
- Háztartás
  - létrehozása
  - törlése
  - módosítása
  - megtekintése
    - neve
    - benne lévő felhasználók
  - felhasználó hozzáadása

# Schemas
## Task
### Részletes
```json
{
    "id": "b49b474d84d745d6a322f7fb3ececfee",
    "title": "Bevásárolni",
    "description": "A listában megadott tételeket megvenni, hazahozni, elpakolni",
    "creation_date": "2024.02.15",
    "due_date": "2024.02.19",
    "done": False,
    "responsibe": None
    "tasks": [
        {
            "id": "d6a322f7fb3ececfeeb49b474d84d745",
            "title": "Alma",
            "type": "checkbox",
            "done": False
        },
        {
            "id": "d6a322f7fb4d84d7453ececfeeb49b47",
            "title": "Körte",
            "type": "checkbox",
            "done": True
        },
    ]
}
```
### Rövid
```json
{
    "id": "b49b474d84d745d6a322f7fb3ececfee",
    "title": "Bevásárolni",
    "due_date": "2024.02.19",
    "done": False,
    "responsibe": None
    "no_tasks": 2
}
```

## User
```json
{
    "id": "45d6a322b49b474d84d7f7fb3ececfee",
    "first_name": "Axel Roland",
    "last_name": "Nemes",
    "email": "neaxro@gmail.com",
    "profile_picture": "sample_1"
}
```

## Háztartás
```json
{
    "id": "19d0914fdb6a4e65b6f04c9f54a646be"
    "title": "Hétköznapok",
    "creation_date": "2024.02.15",
    "people": [
        {"id": "45d6a322b49b474d84d7f7fb3ececfee"},
        {"id": "22b49b474d84d7f7fbecfee345d6a3ec"}
    ],
    "tasks": [
        {"id": "b49b474d84d745d6a322f7fb3ececfee"}
    ]
}
```