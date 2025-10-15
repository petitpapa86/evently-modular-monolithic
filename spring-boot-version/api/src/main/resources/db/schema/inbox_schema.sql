-- Create schemas for all bounded contexts
CREATE SCHEMA IF NOT EXISTS users;
CREATE SCHEMA IF NOT EXISTS events;
CREATE SCHEMA IF NOT EXISTS attendance;
CREATE SCHEMA IF NOT EXISTS ticketing;
CREATE SCHEMA IF NOT EXISTS evently;


-- Create inbox_message_consumers table for idempotent event processing
CREATE TABLE IF NOT EXISTS evently.inbox_message_consumers (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    inbox_message_id UUID NOT NULL,
    name VARCHAR(500) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(inbox_message_id, name)
);

-- Create index for performance
CREATE INDEX IF NOT EXISTS idx_inbox_message_consumers_inbox_message_id
    ON evently.inbox_message_consumers(inbox_message_id);

CREATE INDEX IF NOT EXISTS idx_inbox_message_consumers_name
    ON evently.inbox_message_consumers(name);