package com.evently.modules.attendance.application.attendees.createattendee;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.modules.attendance.application.abstractions.data.IAttendeeRepository;
import com.evently.common.application.IUnitOfWork;
import com.evently.modules.attendance.application.attendees.CreateAttendeeCommand;
import com.evently.modules.attendance.domain.attendees.Attendee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateAttendeeCommandHandler implements ICommandHandler<CreateAttendeeCommand, Void> {
    private final IAttendeeRepository attendeeRepository;
    private final IUnitOfWork unitOfWork;

    public CreateAttendeeCommandHandler(IAttendeeRepository attendeeRepository, @Qualifier("attendanceUnitOfWork") IUnitOfWork unitOfWork) {
        this.attendeeRepository = attendeeRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(CreateAttendeeCommand request) {
        Attendee attendee = Attendee.create(request.attendeeId(), request.email(), request.firstName(), request.lastName());

        attendeeRepository.insert(attendee);

        unitOfWork.saveChanges();

        return Result.success();
    }
}