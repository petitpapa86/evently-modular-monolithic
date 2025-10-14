package com.evently.modules.attendance.infrastructure;

import com.evently.common.infrastructure.outbox.BaseUnitOfWork;
import org.springframework.stereotype.Component;

@Component("attendanceUnitOfWork")
public class UnitOfWork extends BaseUnitOfWork {

    @Override
    protected void doSaveChanges() {
        // In Spring, transaction is managed by @Transactional
    }
}