package com.evently.modules.attendance.application.attendees.getattendee;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Component
@Validated
public class GetAttendeeQueryHandler implements IQueryHandler<GetAttendeeQuery, Optional<Attendee>> {

    private final IAttendeeRepository attendeeRepository;

    public GetAttendeeQueryHandler(IAttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    @Override
    public Result<Optional<Attendee>> handle(GetAttendeeQuery query) {
        Optional<Attendee> attendee = attendeeRepository.findAttendeeById(query.attendeeId());
        return Result.success(attendee);
    }
}