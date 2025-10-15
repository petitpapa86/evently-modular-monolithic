package com.evently.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.evently.api",
    "com.evently.common.infrastructure",
    "com.evently.modules.attendance.application",
    "com.evently.modules.attendance.infrastructure",
    "com.evently.modules.attendance.presentation",
    "com.evently.modules.events.application",
    "com.evently.modules.events.infrastructure",
    "com.evently.modules.events.presentation", 
    "com.evently.modules.ticketing.application",
    "com.evently.modules.ticketing.infrastructure",
    "com.evently.modules.ticketing.presentation",
    "com.evently.modules.users.application",
    "com.evently.modules.users.infrastructure",
    "com.evently.modules.users.presentation"
})
public class EventlyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventlyApiApplication.class, args);
    }
}