package com.evently.modules.events.application.events.updateevent;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.EventErrors;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Component
@Validated
public class UpdateEventCommandHandler implements ICommandHandler<UpdateEventCommand, Void> {

    private final IEventRepository eventRepository;
    private final IUnitOfWork unitOfWork;

    public UpdateEventCommandHandler(IEventRepository eventRepository, @Qualifier("eventsUnitOfWork") IUnitOfWork unitOfWork) {
        this.eventRepository = eventRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(UpdateEventCommand command) {
        Optional<Event> eventOpt = eventRepository.get(command.eventId());
        if (eventOpt.isEmpty()) {
            return Result.failure(EventErrors.notFound(command.eventId()));
        }

        Event event = eventOpt.get();

        // Update properties
        event.setTitle(command.title());
        event.setDescription(command.description());
        event.setLocation(command.location());

        // Reschedule if dates changed
        event.reschedule(command.startsAtUtc(), command.endsAtUtc());

        unitOfWork.saveChanges();

        return Result.success();
    }
}