package com.evently.common.infrastructure.outbox;

public enum OutboxMessageStatus {
    PENDING,
    PROCESSED,
    FAILED
}