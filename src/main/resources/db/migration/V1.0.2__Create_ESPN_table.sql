CREATE TABLE IF NOT EXISTS espn_rankings (
    id              serial primary key,
    rank            INTEGER,
    name            text,
    adp             NUMERIC,
    last_modified   TIMESTAMP
);