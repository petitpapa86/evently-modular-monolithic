package com.evently.common.infrastructure.outbox;

import com.evently.common.application.IUnitOfWork;
import com.evently.common.domain.Entity;
import com.evently.common.domain.IDomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUnitOfWork implements IUnitOfWork {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    private final List<IDomainEvent> domainEvents = new ArrayList<>();

    protected void collectDomainEvents(Entity entity) {
        domainEvents.addAll(entity.getDomainEvents());
        entity.clearDomainEvents();
    }

    protected void collectDomainEvents(Entity... entities) {
        for (Entity entity : entities) {
            collectDomainEvents(entity);
        }
    }

    @Override
    @Transactional
    public void saveChanges() {
        try {
            // Perform the actual save operations (implemented by subclasses)
            doSaveChanges();

            // Publish domain events after successful save
            if (!domainEvents.isEmpty()) {
                domainEventPublisher.publishEvents(new ArrayList<>(domainEvents));
                domainEvents.clear();
            }
        } catch (Exception e) {
            domainEvents.clear(); // Clear events on failure
            throw e;
        }
    }

    protected abstract void doSaveChanges();
}