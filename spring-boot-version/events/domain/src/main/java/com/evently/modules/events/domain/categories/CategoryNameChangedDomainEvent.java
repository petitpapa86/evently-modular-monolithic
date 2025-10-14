package com.evently.modules.events.domain.categories;

import com.evently.common.domain.DomainEvent;
import java.util.UUID;

public class CategoryNameChangedDomainEvent extends DomainEvent {
    private final UUID categoryId;
    private final String name;

    public CategoryNameChangedDomainEvent(UUID categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}