package com.evently.modules.events.infrastructure;

import com.evently.common.infrastructure.outbox.BaseUnitOfWork;
import org.springframework.stereotype.Component;

@Component("eventsUnitOfWork")
public class UnitOfWork extends BaseUnitOfWork {

    @Override
    protected void doSaveChanges() {
        // In Spring, transaction is managed by @Transactional
    }
}