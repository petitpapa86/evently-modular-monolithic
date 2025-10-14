package com.evently.modules.attendance.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import com.evently.modules.attendance.application.attendees.createattendee.CreateAttendeeCommandHandler;
import com.evently.modules.attendance.presentation.attendance.CreateAttendee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

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

    @Bean
    public CreateAttendee createAttendeeEndpoint() {
        return new CreateAttendee(mediator);
    }

    @Bean
    public RouterFunction<ServerResponse> attendanceEndpoints(List<IEndpoint> endpoints) {
        return endpoints.stream()
                .filter(endpoint -> endpoint.getClass().getPackageName().startsWith("com.evently.modules.attendance"))
                .map(IEndpoint::mapEndpoint)
                .reduce(RouterFunction::and)
                .orElseThrow(() -> new IllegalStateException("No attendance endpoints found"));
    }
}