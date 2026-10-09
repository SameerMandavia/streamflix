CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    release_year INTEGER NOT NULL,
    genre VARCHAR(100) NOT NULL,
    poster_url VARCHAR(1000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE seasons (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT NOT NULL REFERENCES series(id) ON DELETE CASCADE,
    season_number INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    CONSTRAINT uq_season_number UNIQUE(series_id, season_number)
);
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    CONSTRAINT uq_episode_number UNIQUE(season_id, episode_number)
);
CREATE TABLE watchlist_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    movie_id BIGINT NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    added_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_watchlist_user_movie UNIQUE(user_id, movie_id)
);
CREATE TABLE watch_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    movie_id BIGINT NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    progress_seconds INTEGER NOT NULL DEFAULT 0,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    last_watched_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_history_user_movie UNIQUE(user_id, movie_id)
);
INSERT INTO series (title, description, release_year, genre, poster_url) VALUES
('Signal / Noise', 'Three strangers decode a transmission that appears in every city at the same impossible hour.', 2025, 'Mystery', 'https://images.unsplash.com/photo-1519608487953-e999c86e7455?auto=format&fit=crop&w=900&q=80');
INSERT INTO seasons (series_id, season_number, title) SELECT id, 1, 'Season One' FROM series WHERE title = 'Signal / Noise';
INSERT INTO episodes (season_id, episode_number, title, description, duration_minutes)
SELECT id, 1, 'The Frequency', 'A late-night radio host discovers a pattern hiding inside the static.', 48 FROM seasons WHERE season_number = 1
UNION ALL SELECT id, 2, 'Dead Air', 'The signal stops, but the people who heard it begin to disappear.', 52 FROM seasons WHERE season_number = 1
UNION ALL SELECT id, 3, 'The Reply', 'A response arrives from beyond the edge of the map.', 55 FROM seasons WHERE season_number = 1;
