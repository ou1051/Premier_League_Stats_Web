# Premier_League_Stats_Web

Premier_League_Stats_Web is a project designed to scrape match statistics for over 660 players, store and serve the data dynamically via a REST API, and eventually predict match outcomes using machine learning. The project is divided into three main components (with a fourth planned): **Data Scraping**, **Database**, **Backend**, and **Frontend** (in progress).

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Components](#components)
  - [Data Scraping](#data-scraping)
  - [Database](#database)
  - [Backend](#backend)
  - [Frontend](#frontend-)
- [Setup & Installation](#setup--installation)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Running the Scraper](#running-the-scraper)
  - [Running the Backend](#running-the-backend)
- [API Endpoints](#api-endpoints)
- [Environment Variables](#environment-variables)

---

## Overview

Premier_League_Stats_Web scrapes player statistics from **FBref** (Premier League Stats page), normalises the data with pandas, and inserts it into a PostgreSQL database. Each collection run is timestamped, so historical snapshots accumulate over time and season-over-season comparisons become possible. A Spring Boot backend then exposes the data through a REST API ready to be consumed by a frontend.

---

## Tech Stack

| Layer        | Technology                              |
|--------------|-----------------------------------------|
| Scraping     | Python 3, `requests`, `pandas`, `BeautifulSoup4` |
| ORM / DB     | SQLAlchemy 2, `psycopg` 3, PostgreSQL 18|
| Backend      | Java 21, Spring Boot 4, Spring Data JPA |
| Frontend     | React (in progress)                  |
| Build tool   | Maven (Spring Boot Maven Plugin)        |

---

## Project Structure

```
Premier_League_Stats_Web/
├── scraper/
│   ├── scraper.py          # FBref → CSV + PostgreSQL pipeline
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

### Data Scraping

**Location:** `scraper/`

The scraper fetches data from **FBref**'s Premier League Stats page:

```
https://fbref.com/en/comps/9/2025-2026/2025-2026-Premier-League-Stats
```

**What it does:**

1. Scrapes the HTML stats tables and parses them with pandas / BeautifulSoup.
2. Selects the relevant columns per player (standard stats, expected goals, defensive actions, etc.).
3. Cleans and normalises column types (xG-family strings → floats, `birth_date` → DATE).
4. Adds `season` (`"2026-27"`) and `collected_at` (current timestamp floored to the second).
5. Saves a dated CSV snapshot to `scraper/data/fpl_players_YYYY-MM-DD.csv`.
6. Inserts all rows into PostgreSQL via SQLAlchemy (`if_exists='append'`), capturing **667 players** per run.

**Columns collected:**

`player_id`, `first_name`, `second_name`, `web_name`, `team`, `position`, `birth_date`, `minutes`, `starts`, `goals_scored`, `assists`, `penalties_missed`, `clean_sheets`, `goals_conceded`, `own_goals`, `saves`, `penalties_saved`, `tackles`, `recoveries`, `clearances_blocks_interceptions`, `yellow_cards`, `red_cards`, `expected_goals`, `expected_assists`, `expected_goal_involvements`, `expected_goals_conceded`, `season`, `collected_at`

---

### Database

**Location:** `db/schema.sql`

- **Database:** `pl_stats` (PostgreSQL 18, managed via pgAdmin)
- **Table:** `player_stats`
- **Primary key:** `(season, player_id, collected_at)` — one row per player per collection run, enabling time-series tracking across the season.

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
- **Connects to:** PostgreSQL via `application.properties` (credentials injected via environment variables)

**Key classes:**

| Class | Role |
|---|---|
| `Player` | JPA entity mapped to `player_stats` |
| `PlayerId` | Composite primary key class `(season, player_id, collected_at)` |
| `PlayerRepository` | Spring Data JPA repository |
| `PlayerService` | Business logic — filtering by team, position, name |
| `PlayerController` | REST controller, base path `/api/players` |

---

### Frontend 

**Status: Work in progress — not yet implemented.**

The frontend will be built with **React** and will consume the Spring Boot REST API to display player statistics, filter by team/position, and visualise season trends.

---

## Setup & Installation

### Prerequisites

- Python 3.10+
- Java 21 (Amazon Corretto recommended)
- Maven
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

### Running the Scraper

```bash
cd scraper
pip install -r requirements.txt

# Set required env var
export DB_PASSWORD=your_password
# Optional (defaults to "postgres")
export DB_USERNAME=your_username

python scraper.py
```

A CSV is saved to `scraper/data/` and all rows are inserted into `player_stats`.

### Running the Backend

```bash
cd backend

# Set required env vars
export DB_PASSWORD=your_password
export DB_USERNAME=your_username   # defaults to "postgres"

./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## API Endpoints

Base path: `/api/players`

| Method | Path | Query params | Description |
|--------|------|-------------|-------------|
| `GET` | `/api/players` | — | All players (latest snapshot) |
| `GET` | `/api/players` | `?team=Arsenal` | Filter by team |
| `GET` | `/api/players` | `?position=FWD` | Filter by position |
| `GET` | `/api/players` | `?team=Arsenal&position=FWD` | Filter by team + position |
| `GET` | `/api/players` | `?name=Salah` | Search by name |
| `GET` | `/api/players/{id}` | — | Single player by ID |
| `POST` | `/api/players` | — | Create a player record |
| `PUT` | `/api/players/{id}` | — | Update a player record |
| `DELETE` | `/api/players/{id}` | — | Delete a player record |

---

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `DB_PASSWORD` | Yes | — | PostgreSQL password |
| `DB_USERNAME` | No | `postgres` | PostgreSQL username |
