-- Player season stats collected from the FPL API.
-- One row per player per collection run, so stats can be compared over time.
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

    -- The same player can appear once per collection run, never twice in the same run
                                            PRIMARY KEY (season, player_id, collected_at)
);