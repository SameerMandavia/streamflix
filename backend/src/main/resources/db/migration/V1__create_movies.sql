CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    release_year INTEGER NOT NULL,
    duration_minutes INTEGER NOT NULL,
    genre VARCHAR(100) NOT NULL,
    poster_url VARCHAR(1000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_movies_created_at ON movies (created_at DESC);
