package com.evently.modules.events.domain.categories;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class CategoryArchivedDomainEvent extends DomainEvent {
    private final UUID categoryId;

    public CategoryArchivedDomainEvent(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }
}