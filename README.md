# Premier_League_Stats_Web

Premier_League_Stats_Web collects season statistics for every Premier League player (about 660 per run) from the official **Fantasy Premier League (FPL) API**, stores each collection run in PostgreSQL, and serves the data through a Spring Boot REST API. A React frontend is in progress, and match-outcome prediction with machine learning is planned as a later extension.

The project has four components: **Data Collection**, **Database**, **Backend**, and **Frontend** (in progress).

---

## Table of Contents

- [Overview](#overview)
- [Data Source](#data-source)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Components](#components)
  - [Data Collection](#data-collection)
  - [Database](#database)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Setup & Installation](#setup--installation)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Running the Data Collector](#running-the-data-collector)
  - [Running the Backend](#running-the-backend)
- [API Endpoints](#api-endpoints)
- [Environment Variables](#environment-variables)
- [Roadmap](#roadmap)

---

## Overview

A Python script requests player data from the FPL API, cleans and normalises it with pandas, and writes it both to a dated CSV snapshot and to a PostgreSQL database. Every run is timestamped (`collected_at`), so snapshots accumulate over the season and make it possible to track how a player's numbers develop over time. A Spring Boot backend reads the most recent snapshot and exposes it through a REST API for the frontend.

---

## Data Source

All data comes from the public FPL endpoint:

```
https://fantasy.premierleague.com/api/bootstrap-static/
```

This endpoint returns JSON containing every player (`elements`), every club (`teams`) and the position types (`element_types`). Column names such as `web_name`, `element_type` and the numeric `id` fields are FPL's own naming.

**Why FPL instead of FBref:** the project originally planned to scrape the HTML tables on FBref's Premier League Stats page. FBref blocked automated requests, so the data source was switched to the FPL API. This turned out to be the better choice: the data arrives as structured JSON instead of HTML, it is updated during the season, and it already includes expected-goals metrics (xG, xA, xGI, xGC).

---

## Tech Stack

| Layer           | Technology                                   |
|-----------------|----------------------------------------------|
| Data collection | Python 3, `requests`, `pandas`               |
| ORM / DB        | SQLAlchemy 2, `psycopg` 3, PostgreSQL 18     |
| Backend         | Java 21, Spring Boot 4, Spring Data JPA      |
| Frontend        | React (in progress)                          |
| Build tool      | Maven (Spring Boot Maven Plugin)             |

---

## Project Structure

```
Premier_League_Stats_Web/
├── scraper/
│   ├── scraper.py          # FPL API → CSV + PostgreSQL pipeline
│   └── requirements.txt    # Python dependencies
├── db/
│   └── schema.sql          # PostgreSQL table definition
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/premierleague/
│       ├── Application.java
│       └── player/
│           ├── Player.java
│           ├── PlayerId.java
│           ├── PlayerController.java
│           ├── PlayerRepository.java
│           └── PlayerService.java
└── README.md
```

---

## Components

### Data Collection

**Location:** `scraper/scraper.py`

**What it does:**

1. Sends a GET request to the FPL `bootstrap-static` endpoint and parses the JSON response.
2. Converts the player list (`elements`) into a pandas DataFrame and keeps only the relevant columns (standard stats, expected-goals metrics, defensive actions, discipline).
3. Maps FPL IDs to readable values:
   - `team` ID → club name (e.g. `1` → `Arsenal`)
   - `element_type` ID → position (`GKP`, `DEF`, `MID`, `FWD`), renamed to `position`
   - `id` is renamed to `player_id` to match the database schema
4. Converts types: expected-goals columns from strings to floats, `birth_date` from string to date.
5. Adds `season` (`"2026-27"`) and `collected_at` (one timestamp per run, floored to the second).
6. Saves a dated CSV snapshot to `scraper/data/fpl_players_YYYY-MM-DD.csv`.
7. Appends all rows to the `player_stats` table via SQLAlchemy (`if_exists='append'`); a typical run inserts about 667 players.

**Columns stored:**

`player_id`, `first_name`, `second_name`, `web_name`, `team`, `position`, `birth_date`, `minutes`, `starts`, `goals_scored`, `assists`, `penalties_missed`, `clean_sheets`, `goals_conceded`, `own_goals`, `saves`, `penalties_saved`, `tackles`, `recoveries`, `clearances_blocks_interceptions`, `yellow_cards`, `red_cards`, `expected_goals`, `expected_assists`, `expected_goal_involvements`, `expected_goals_conceded`, `season`, `collected_at`

---

### Database

**Location:** `db/schema.sql`

- **Database:** `pl_stats` (PostgreSQL 18, managed via pgAdmin)
- **Table:** `player_stats`
- **Primary key:** `(season, player_id, collected_at)`: one row per player per collection run, which enables time-series tracking across the season.

```sql
CREATE TABLE IF NOT EXISTS player_stats (
    player_id                        INTEGER       NOT NULL,
    first_name                       TEXT          NOT NULL,
    second_name                      TEXT          NOT NULL,
    web_name                         TEXT          NOT NULL,
    team                             TEXT          NOT NULL,
    position                         TEXT          NOT NULL,
    birth_date                       DATE,
    minutes                          INTEGER       NOT NULL,
    starts                           INTEGER       NOT NULL,
    goals_scored                     INTEGER       NOT NULL,
    assists                          INTEGER       NOT NULL,
    penalties_missed                 INTEGER       NOT NULL,
    clean_sheets                     INTEGER       NOT NULL,
    goals_conceded                   INTEGER       NOT NULL,
    own_goals                        INTEGER       NOT NULL,
    saves                            INTEGER       NOT NULL,
    penalties_saved                  INTEGER       NOT NULL,
    tackles                          INTEGER       NOT NULL,
    recoveries                       INTEGER       NOT NULL,
    clearances_blocks_interceptions  INTEGER       NOT NULL,
    yellow_cards                     INTEGER       NOT NULL,
    red_cards                        INTEGER       NOT NULL,
    expected_goals                   NUMERIC(6, 2) NOT NULL,
    expected_assists                 NUMERIC(6, 2) NOT NULL,
    expected_goal_involvements       NUMERIC(6, 2) NOT NULL,
    expected_goals_conceded          NUMERIC(6, 2) NOT NULL,
    season                           TEXT          NOT NULL,
    collected_at                     TIMESTAMP     NOT NULL,
    PRIMARY KEY (season, player_id, collected_at)
);
```

---

### Backend

**Location:** `backend/`

A **Spring Boot 4 / Java 21** REST API built with Spring Data JPA.

- **Package:** `com.premierleague`
- **Entry point:** `Application.java`
- **Database connection:** configured in `application.properties`; credentials are injected through environment variables
- **Schema handling:** `spring.jpa.hibernate.ddl-auto=validate`, so the entity must match `db/schema.sql` and Hibernate never modifies the table

**Key classes:**

| Class | Role |
|---|---|
| `Player` | JPA entity mapped to `player_stats` |
| `PlayerId` | Composite primary key class `(season, player_id, collected_at)` |
| `PlayerRepository` | Spring Data JPA repository; finds the latest `collected_at` and filters within that run |
| `PlayerService` | Business logic: read queries are scoped to the latest collection run, plus filtering by team, position and name |
| `PlayerController` | REST controller, base path `/api/players` |

---

### Frontend

**Status: work in progress.**

The frontend will be built with **React**. It will consume the REST API to display player statistics, filter by team and position, and visualise how numbers develop over the season.

---

## Setup & Installation

### Prerequisites

- Python 3.10+
- Java 21 (Amazon Corretto recommended)
- Maven (or the included Maven wrapper `./mvnw`)
- PostgreSQL 18
- pgAdmin (optional, for DB management)

### Database Setup

1. Create the database:
   ```sql
   CREATE DATABASE pl_stats;
   ```

2. Run the schema:
   ```bash
   psql -U postgres -d pl_stats -f db/schema.sql
   ```

### Running the Data Collector

```bash
cd scraper
pip install -r requirements.txt

# Required
export DB_PASSWORD=your_password
# Optional (defaults to "postgres")
export DB_USERNAME=your_username

python scraper.py
```

A CSV snapshot is saved to `scraper/data/` (ignored by Git) and all rows are appended to `player_stats`. The script is currently run manually; each run adds a new snapshot.

### Running the Backend

```bash
cd backend

# Required
export DB_PASSWORD=your_password
# Optional (defaults to "postgres")
export DB_USERNAME=your_username

./mvnw spring-boot:run
```

The API is then available at `http://localhost:8080`.

---

## API Endpoints

Base path: `/api/players`. Read endpoints return data from the most recent collection run.

| Method | Path | Query params | Description |
|--------|------|-------------|-------------|
| `GET` | `/api/players` | none | All players (latest snapshot) |
| `GET` | `/api/players` | `?team=Arsenal` | Filter by team |
| `GET` | `/api/players` | `?position=FWD` | Filter by position (`GKP`, `DEF`, `MID`, `FWD`) |
| `GET` | `/api/players` | `?team=Arsenal&position=FWD` | Filter by team and position |
| `GET` | `/api/players` | `?name=Salah` | Case-insensitive search on `web_name` |
| `GET` | `/api/players/{id}` | none | Latest record of a single player by FPL player ID |
| `POST` | `/api/players` | none | Create a player record |
| `PUT` | `/api/players/{id}` | none | Update the latest record of a player |
| `DELETE` | `/api/players/{id}` | none | Delete all records of a player |

---

## Environment Variables

| Variable | Required | Default | Used by | Description |
|----------|----------|---------|---------|-------------|
| `DB_PASSWORD` | Yes | none | Data collector, backend | PostgreSQL password |
| `DB_USERNAME` | No | `postgres` | Data collector, backend | PostgreSQL username |

---

## Roadmap

- Schedule the data collector (cron or GitHub Actions) instead of running it manually
- Finish the React frontend
- Ranking endpoints (e.g. top scorers, top xG)
- Match-outcome prediction with machine learning
