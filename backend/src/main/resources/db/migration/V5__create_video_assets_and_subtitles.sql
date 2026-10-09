CREATE TABLE video_assets (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL UNIQUE REFERENCES movies(id) ON DELETE CASCADE,
    source_path VARCHAR(1000) NOT NULL,
    hls_path VARCHAR(1000),
    status VARCHAR(30) NOT NULL,
    error_message VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE subtitles (
    id BIGSERIAL PRIMARY KEY,
    video_asset_id BIGINT NOT NULL REFERENCES video_assets(id) ON DELETE CASCADE,
    language_code VARCHAR(12) NOT NULL,
    label VARCHAR(80) NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    CONSTRAINT uq_subtitle_asset_language UNIQUE(video_asset_id, language_code)
);
