package com.evently.common.infrastructure.clock;

import com.evently.common.application.IDateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeProvider implements IDateTimeProvider {
    @Override
    public LocalDateTime utcNow() {
        return LocalDateTime.now(java.time.ZoneOffset.UTC);
    }
}