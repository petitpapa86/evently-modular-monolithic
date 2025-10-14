package com.evently.common.application.clock;

import java.time.LocalDateTime;

public interface IDateTimeProvider {
    LocalDateTime utcNow();
}