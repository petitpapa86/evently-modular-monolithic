package com.evently.modules.attendance.application.attendees.getattendees;

import com.evently.common.application.messaging.IQueryHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
public class GetAttendeesQueryHandler implements IQueryHandler<GetAttendeesQuery, List<Attendee>> {

    private final IAttendeeRepository attendeeRepository;

    public GetAttendeesQueryHandler(IAttendeeRepository attendeeRepository) {
        this.attendeeRepository = attendeeRepository;
    }

    @Override
    public Result<List<Attendee>> handle(GetAttendeesQuery query) {
        List<Attendee> attendees = attendeeRepository.getAll();
        return Result.success(attendees);
    }
}