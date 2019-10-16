CREATE TABLE IF NOT EXISTS drafted_player (
  id                serial primary key,
  drafted_league    INTEGER,
  drafted_team      INTEGER,
  drafted_team_name TEXT,
  drafted_pos       INTEGER,
  rank              INTEGER,
  name              text,
  pos               text,
  gp                INTEGER,
  mpg               NUMERIC,
  fg_percent        NUMERIC,
  ft_percent        NUMERIC,
  threepm           NUMERIC,
  pts               NUMERIC,
  treb              NUMERIC,
  ast               NUMERIC,
  stl               NUMERIC,
  blk               NUMERIC,
  turnovers         NUMERIC,
  total             NUMERIC,
  last_modified     TIMESTAMP

);

CREATE TABLE IF NOT EXISTS drafting_room (
  id              serial primary key,
  league          INTEGER,
  espn_rank       INTEGER,
  espn_adp        NUMERIC,
  hashtag_rank    INTEGER,
  name            text,
  pos             text,
  gp              INTEGER,
  mpg             NUMERIC,
  fg_percent      NUMERIC,
  ft_percent      NUMERIC,
  threepm         NUMERIC,
  pts             NUMERIC,
  treb            NUMERIC,
  ast             NUMERIC,
  stl             NUMERIC,
  blk             NUMERIC,
  turnovers       NUMERIC,
  total           NUMERIC,
  last_modified   TIMESTAMP

);