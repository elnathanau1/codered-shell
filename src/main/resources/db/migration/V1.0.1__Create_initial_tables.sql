CREATE TABLE IF NOT EXISTS hashtag_rankings (
  id              serial primary key,
  rank            INTEGER,
  name            text,
  pos             text,
  team            text,
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