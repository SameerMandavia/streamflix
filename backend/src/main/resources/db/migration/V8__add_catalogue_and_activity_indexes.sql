CREATE INDEX idx_movies_published_created_at ON movies (published, created_at DESC);
CREATE INDEX idx_movies_genre ON movies (genre);
CREATE INDEX idx_series_published_created_at ON series (published, created_at DESC);
CREATE INDEX idx_watch_history_user_last_watched ON watch_history (user_id, last_watched_at DESC);
CREATE INDEX idx_watchlist_user_added ON watchlist_items (user_id, added_at DESC);
CREATE INDEX idx_subscriptions_status ON subscriptions (status);
