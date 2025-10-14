package com.evently.modules.users.infrastructure;

import com.evently.common.infrastructure.outbox.BaseUnitOfWork;
import org.springframework.stereotype.Component;

@Component("usersUnitOfWork")
public class UnitOfWork extends BaseUnitOfWork {

    @Override
    protected void doSaveChanges() {
        // In Spring Data JPA, changes are automatically saved when methods return
        // The @Transactional annotation ensures atomicity
    }
}