package com.evently.modules.events.domain.categories;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class CategoryCreatedDomainEvent extends DomainEvent {
    private final UUID categoryId;

    public CategoryCreatedDomainEvent(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }
}