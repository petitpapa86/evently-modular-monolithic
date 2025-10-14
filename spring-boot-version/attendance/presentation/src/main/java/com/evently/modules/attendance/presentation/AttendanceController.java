package com.evently.modules.attendance.presentation;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attendees")
public class AttendanceController {
    private final IMediator mediator;

    public AttendanceController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<String> createAttendee(@RequestBody CreateAttendeeRequest request) {
        CreateAttendeeCommand command = new CreateAttendeeCommand(
                UUID.randomUUID(),
                request.email(),
                request.firstName(),
                request.lastName()
        );

        Result<Void> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("Attendee created");
        } else {
            return ResponseEntity.badRequest().body("Error creating attendee");
        }
    }

    public record CreateAttendeeRequest(String email, String firstName, String lastName) {
    }
}