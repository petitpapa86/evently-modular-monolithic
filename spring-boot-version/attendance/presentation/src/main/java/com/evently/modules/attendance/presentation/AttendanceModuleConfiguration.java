package com.evently.modules.attendance.presentation;

import com.evently.common.application.Mediator;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import com.evently.modules.attendance.application.attendees.createattendee.CreateAttendeeCommandHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import jakarta.annotation.PostConstruct;

@Configuration
@Import(CreateAttendeeCommandHandler.class)
public class AttendanceModuleConfiguration {

    private final Mediator mediator;
    private final CreateAttendeeCommandHandler createAttendeeHandler;

    public AttendanceModuleConfiguration(Mediator mediator, CreateAttendeeCommandHandler createAttendeeHandler) {
        this.mediator = mediator;
        this.createAttendeeHandler = createAttendeeHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(CreateAttendeeCommand.class, createAttendeeHandler);
    }
}