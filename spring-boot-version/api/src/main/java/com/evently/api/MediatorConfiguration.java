package com.evently.api;

import com.evently.common.application.Mediator;
import com.evently.modules.attendance.presentation.AttendanceModuleConfiguration;
import com.evently.modules.events.presentation.EventsModuleConfiguration;
import com.evently.modules.ticketing.presentation.TicketingModuleConfiguration;
import com.evently.modules.users.presentation.UsersModuleConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    AttendanceModuleConfiguration.class,
    EventsModuleConfiguration.class,
    TicketingModuleConfiguration.class,
    UsersModuleConfiguration.class
})
public class MediatorConfiguration {

    @Bean
    public Mediator mediator() {
        return new Mediator();
    }
}