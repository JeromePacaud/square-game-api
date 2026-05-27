CREATE TABLE IF NOT EXISTS game (
    id VARCHAR(150) PRIMARY KEY,
    type TEXT,
    player_count INT,
    board_size INT,
    player_ids TEXT
);