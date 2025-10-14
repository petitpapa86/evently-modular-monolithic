package com.evently.modules.events.application.events.deleteevent;

import com.evently.common.application.ICommandHandler;
import com.evently.common.application.IDateTimeProvider;
import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Result;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.EventErrors;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class DeleteEventCommandHandler implements ICommandHandler<DeleteEventCommand, Void> {

    private final IEventRepository eventRepository;
    private final IDateTimeProvider dateTimeProvider;
    private final IUnitOfWork unitOfWork;

    public DeleteEventCommandHandler(IEventRepository eventRepository, IDateTimeProvider dateTimeProvider, @Qualifier("eventsUnitOfWork") IUnitOfWork unitOfWork) {
        this.eventRepository = eventRepository;
        this.dateTimeProvider = dateTimeProvider;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<Void> handle(DeleteEventCommand command) {
        Optional<Event> eventOpt = eventRepository.get(command.eventId());
        if (eventOpt.isEmpty()) {
            return Result.failure(EventErrors.notFound(command.eventId()));
        }

        Event event = eventOpt.get();

        Result cancelResult = event.cancel(dateTimeProvider.utcNow());
        if (cancelResult.isFailure()) {
            return cancelResult;
        }

        unitOfWork.saveChanges();

        return Result.success();
    }
}