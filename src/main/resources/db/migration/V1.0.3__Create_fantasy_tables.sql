CREATE TABLE IF NOT EXISTS league (
    id              serial primary key,
    name            text,
    num_teams       INTEGER,
    last_modified   TIMESTAMP
);

CREATE TABLE IF NOT EXISTS team (
    id              serial primary key,
    name            text,
    league_id       integer

);
