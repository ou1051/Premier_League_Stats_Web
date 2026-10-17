import os
import requests
import pandas as pd
from pathlib import Path
from sqlalchemy import create_engine

URL = "https://fantasy.premierleague.com/api/bootstrap-static/"

res = requests.get(URL, timeout=30)
res.raise_for_status() # for error
data = res.json()

COLUMNS = [
    "id", "first_name", "second_name", "web_name", "team", "element_type", "birth_date",
    "minutes", "starts", "goals_scored", "assists", "penalties_missed",
    "clean_sheets", "goals_conceded", "own_goals", "saves", "penalties_saved",
    "tackles", "recoveries", "clearances_blocks_interceptions",
    "yellow_cards", "red_cards",
    "expected_goals", "expected_assists", "expected_goal_involvements", "expected_goals_conceded",
] # columns to keep for the CSV

SEASON = "2026-27"
XG_COLUMNS = [
    "expected_goals", "expected_assists",
    "expected_goal_involvements", "expected_goals_conceded",
] # elements that need to string -> float

players = pd.DataFrame(data["elements"]) # convert player list to DataFrame
players = players[COLUMNS] # selecting what we need for csv

teams = {t["id"]: t["name"] for t in data["teams"]} # {1: "Arsenal", 2: "Aston Villa", 3: "Bournemouth", ...}
positions = {p["id"]: p["singular_name_short"] for p in data["element_types"]} # {1: "GKP", 2: "DEF", 3: "MID", 4: "FWD"}

players["team"] = players["team"].map(teams) # team ID -> team name
players["element_type"] = players["element_type"].map(positions) # element type -> positions
players = players.rename(columns={"element_type": "position"}) # rename column
players = players.rename(columns={"id": "player_id"}) #Postgred only had player_id not id
players[XG_COLUMNS] = players[XG_COLUMNS].apply(pd.to_numeric) # string -> float
players["birth_date"] = pd.to_datetime(players["birth_date"])

players["season"] = SEASON # add a new column called season
collected_at = pd.Timestamp.now().floor("s")  # take the timestamp once and reuse it
players["collected_at"] = collected_at

output_dir = Path(__file__).parent / "data"
output_dir.mkdir(exist_ok=True)

file_name = f"fpl_players_{collected_at:%Y-%m-%d}.csv"
output_path = output_dir / file_name

players.to_csv(output_path, index=False)
print(f"Saved {len(players)} players to {output_path}")




DB_USERNAME = os.environ.get("DB_USERNAME", "postgres")
DB_PASSWORD = os.environ["DB_PASSWORD"]
engine = create_engine(f'postgresql://{DB_USERNAME}:{DB_PASSWORD}@localhost:5432/pl_stats') # Connect to Postgres

players.to_sql('player_stats', con=engine, if_exists='append', index=False) # insert dataframe to Postgres
print(f"Successfully inserted {len(players)} rows into PostgreSQL")

