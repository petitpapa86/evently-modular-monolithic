package com.evently.modules.ticketing.infrastructure;

import com.evently.common.infrastructure.outbox.BaseUnitOfWork;
import org.springframework.stereotype.Component;

@Component("ticketingUnitOfWork")
public class UnitOfWork extends BaseUnitOfWork {

    @Override
    protected void doSaveChanges() {
        // Spring's @Transactional handles the transaction management
        // No additional implementation needed as Spring manages the EntityManager
    }
}