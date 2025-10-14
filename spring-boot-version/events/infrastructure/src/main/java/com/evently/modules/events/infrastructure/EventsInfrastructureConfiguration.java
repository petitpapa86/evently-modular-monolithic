package com.evently.modules.events.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.evently.modules.events.infrastructure")
@EntityScan(basePackages = "com.evently.modules.events.infrastructure")
public class EventsInfrastructureConfiguration {
}