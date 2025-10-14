package com.evently.modules.events.application.events.createevent;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.common.application.IDateTimeProvider;
import com.evently.common.application.IUnitOfWork;
import com.evently.modules.events.domain.categories.Category;
import com.evently.modules.events.domain.categories.ICategoryRepository;
import com.evently.modules.events.domain.events.Event;
import com.evently.modules.events.domain.events.EventErrors;
import com.evently.modules.events.domain.events.IEventRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateEventCommandHandler implements ICommandHandler<CreateEventCommand, UUID> {

    private final IDateTimeProvider dateTimeProvider;
    private final ICategoryRepository categoryRepository;
    private final IEventRepository eventRepository;
    private final IUnitOfWork unitOfWork;

    public CreateEventCommandHandler(IDateTimeProvider dateTimeProvider, ICategoryRepository categoryRepository, IEventRepository eventRepository, @Qualifier("eventsUnitOfWork") IUnitOfWork unitOfWork) {
        this.dateTimeProvider = dateTimeProvider;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    @Transactional
    public Result<UUID> handle(CreateEventCommand request) {
        if (request.startsAtUtc().isBefore(dateTimeProvider.utcNow())) {
            return Result.failure(EventErrors.START_DATE_IN_PAST);
        }

        Optional<Category> categoryOpt = categoryRepository.get(request.categoryId());
        if (categoryOpt.isEmpty()) {
            return Result.failure(EventErrors.notFound(request.categoryId()));
        }

        Category category = categoryOpt.get();
        Result<Event> result = Event.create(
            category,
            request.title(),
            request.description(),
            request.location(),
            request.startsAtUtc(),
            request.endsAtUtc());

        if (result.isFailure()) {
            return Result.failure(result.getErrors().get(0));
        }

        Event event = result.getValue();
        eventRepository.insert(event);

        unitOfWork.saveChanges();

        return Result.success(event.getId());
    }
}