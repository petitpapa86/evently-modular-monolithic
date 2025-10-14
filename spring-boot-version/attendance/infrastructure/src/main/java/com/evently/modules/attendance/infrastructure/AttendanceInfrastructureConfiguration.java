package com.evently.modules.attendance.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.evently.modules.attendance.infrastructure")
@EntityScan(basePackages = "com.evently.modules.attendance.infrastructure")
public class AttendanceInfrastructureConfiguration {
}