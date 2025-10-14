package com.evently.modules.events.presentation;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.modules.events.application.events.createevent.CreateEventCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventsController {
    private final IMediator mediator;

    public EventsController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventRequest request) {
        CreateEventCommand command = new CreateEventCommand(
                request.categoryId(),
                request.title(),
                request.description(),
                request.location(),
                request.startsAtUtc(),
                request.endsAtUtc()
        );

        Result<UUID> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("Event created with ID: " + result.getValue());
        } else {
            return ResponseEntity.badRequest().body("Error creating event");
        }
    }

    public record CreateEventRequest(
            UUID categoryId,
            String title,
            String description,
            String location,
            LocalDateTime startsAtUtc,
            Optional<LocalDateTime> endsAtUtc) {
    }
}