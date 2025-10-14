package com.evently.modules.ticketing.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.evently.modules.ticketing.infrastructure")
@EntityScan(basePackages = "com.evently.modules.ticketing.infrastructure")
public class TicketingInfrastructureConfiguration {
}