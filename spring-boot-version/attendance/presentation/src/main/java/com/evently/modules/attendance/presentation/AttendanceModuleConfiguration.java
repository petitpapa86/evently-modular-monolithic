package com.evently.modules.attendance.presentation;

import com.evently.common.application.Mediator;
import com.evently.common.presentation.endpoints.IEndpoint;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import com.evently.modules.attendance.application.attendees.createattendee.CreateAttendeeCommandHandler;
import com.evently.modules.attendance.application.attendees.getattendee.GetAttendeeQuery;
import com.evently.modules.attendance.application.attendees.getattendee.GetAttendeeQueryHandler;
import com.evently.modules.attendance.application.attendees.getattendees.GetAttendeesQuery;
import com.evently.modules.attendance.application.attendees.getattendees.GetAttendeesQueryHandler;
import com.evently.modules.attendance.presentation.attendance.CreateAttendee;
import com.evently.modules.attendance.presentation.attendance.GetAttendee;
import com.evently.modules.attendance.presentation.attendance.GetAttendees;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Configuration
@Import({CreateAttendeeCommandHandler.class, GetAttendeeQueryHandler.class, GetAttendeesQueryHandler.class})
public class AttendanceModuleConfiguration {

    private final Mediator mediator;
    private final CreateAttendeeCommandHandler createAttendeeHandler;
    private final GetAttendeeQueryHandler getAttendeeHandler;
    private final GetAttendeesQueryHandler getAttendeesHandler;

    public AttendanceModuleConfiguration(Mediator mediator,
                                       CreateAttendeeCommandHandler createAttendeeHandler,
                                       GetAttendeeQueryHandler getAttendeeHandler,
                                       GetAttendeesQueryHandler getAttendeesHandler) {
        this.mediator = mediator;
        this.createAttendeeHandler = createAttendeeHandler;
        this.getAttendeeHandler = getAttendeeHandler;
        this.getAttendeesHandler = getAttendeesHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        mediator.registerCommandHandler(CreateAttendeeCommand.class, createAttendeeHandler);
        mediator.registerQueryHandler(GetAttendeeQuery.class, getAttendeeHandler);
        mediator.registerQueryHandler(GetAttendeesQuery.class, getAttendeesHandler);
    }

    @Bean
    public CreateAttendee createAttendeeEndpoint() {
        return new CreateAttendee(mediator);
    }

    @Bean
    public GetAttendee getAttendeeEndpoint() {
        return new GetAttendee(mediator);
    }

    @Bean
    public GetAttendees getAttendeesEndpoint() {
        return new GetAttendees(mediator);
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