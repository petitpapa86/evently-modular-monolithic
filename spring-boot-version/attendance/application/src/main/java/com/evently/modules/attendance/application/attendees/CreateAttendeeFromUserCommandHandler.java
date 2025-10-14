package com.evently.modules.attendance.application.attendees;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.common.application.IUnitOfWork;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class CreateAttendeeFromUserCommandHandler implements ICommandHandler<CreateAttendeeFromUserCommand, Void> {
    private final IAttendeeRepository attendeeRepository;
    private final IUnitOfWork unitOfWork;

    public CreateAttendeeFromUserCommandHandler(IAttendeeRepository attendeeRepository, @Qualifier("attendanceUnitOfWork") IUnitOfWork unitOfWork) {
        this.attendeeRepository = attendeeRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(CreateAttendeeFromUserCommand request) {
        // Check if attendee already exists
        if (attendeeRepository.findAttendeeById(request.userId()).isPresent()) {
            return Result.success(null); // Attendee already exists, nothing to do
        }

        Attendee attendee = Attendee.create(request.userId(), request.email(), request.firstName(), request.lastName());

        attendeeRepository.insert(attendee);

        unitOfWork.saveChanges();

        return Result.success(null);
    }
}