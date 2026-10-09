CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    name VARCHAR(80) NOT NULL,
    avatar_key VARCHAR(40) NOT NULL DEFAULT 'sunset',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_profiles_user_name UNIQUE (user_id, name)
);

CREATE INDEX idx_profiles_user_id ON profiles (user_id);
