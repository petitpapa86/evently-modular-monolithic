package com.evently.modules.users.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.evently.modules.users.infrastructure")
@EntityScan(basePackages = "com.evently.modules.users.infrastructure")
public class UsersInfrastructureConfiguration {
}