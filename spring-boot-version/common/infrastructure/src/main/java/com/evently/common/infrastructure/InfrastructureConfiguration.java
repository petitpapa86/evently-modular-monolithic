package com.evently.common.infrastructure;

import com.evently.common.application.IEventBus;
import com.evently.common.infrastructure.outbox.OutboxEventBus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.evently.common.infrastructure.outbox")
@EntityScan(basePackages = "com.evently.common.infrastructure.outbox")
public class InfrastructureConfiguration {

    @Bean
    @Primary
    public IEventBus eventBus(OutboxEventBus outboxEventBus) {
        return outboxEventBus;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}