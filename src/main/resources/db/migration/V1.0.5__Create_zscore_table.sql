CREATE TABLE IF NOT EXISTS hashtag_rankings_zscore (
  id              serial primary key,
  name            text,
  fg_percent      NUMERIC,
  ft_percent      NUMERIC,
  threepm         NUMERIC,
  pts             NUMERIC,
  treb            NUMERIC,
  ast             NUMERIC,
  stl             NUMERIC,
  blk             NUMERIC,
  turnovers       NUMERIC
);