-- Create outbox_messages table for reliable event publishing
-- Note: Schema 'evently' is already created in inbox_schema.sql

CREATE TABLE IF NOT EXISTS evently.outbox_messages (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    message_id UUID NOT NULL UNIQUE,
    type VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    occurred_on_utc TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_on_utc TIMESTAMP NULL,
    error TEXT NULL,
    retry_count INT DEFAULT 0,
    correlation_id UUID NULL,
    causation_id UUID NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_outbox_messages_type
    ON evently.outbox_messages(type);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_processed_on_utc
    ON evently.outbox_messages(processed_on_utc);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_occurred_on_utc
    ON evently.outbox_messages(occurred_on_utc);

CREATE INDEX IF NOT EXISTS idx_outbox_messages_correlation_id
    ON evently.outbox_messages(correlation_id);

-- Create index for unprocessed messages (most common query)
CREATE INDEX IF NOT EXISTS idx_outbox_messages_unprocessed
    ON evently.outbox_messages(processed_on_utc)
    WHERE processed_on_utc IS NULL;