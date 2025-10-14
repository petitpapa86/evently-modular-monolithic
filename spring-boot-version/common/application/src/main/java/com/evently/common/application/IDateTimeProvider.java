package com.evently.common.application;

import java.time.LocalDateTime;

public interface IDateTimeProvider {
    LocalDateTime utcNow();
}