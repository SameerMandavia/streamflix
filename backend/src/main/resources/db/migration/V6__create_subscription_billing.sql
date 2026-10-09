CREATE TABLE plans (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    price_cents INTEGER NOT NULL,
    currency VARCHAR(3) NOT NULL,
    billing_interval VARCHAR(20) NOT NULL,
    stripe_price_id VARCHAR(255),
    premium BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    plan_id BIGINT NOT NULL REFERENCES plans(id),
    stripe_customer_id VARCHAR(255),
    stripe_subscription_id VARCHAR(255) UNIQUE,
    status VARCHAR(40) NOT NULL,
    current_period_end TIMESTAMPTZ,
    cancel_at_period_end BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE processed_webhook_events (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL UNIQUE,
    received_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
INSERT INTO plans (code, name, price_cents, currency, billing_interval, stripe_price_id, premium) VALUES
('free', 'Free', 0, 'INR', 'month', NULL, FALSE),
('basic', 'Basic', 999, 'INR', 'month', 'price_replace_with_stripe_test_basic', TRUE),
('premium', 'Premium', 1499, 'INR', 'month', 'price_replace_with_stripe_test_premium', TRUE);
