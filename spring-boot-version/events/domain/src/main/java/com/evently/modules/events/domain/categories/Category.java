package com.evently.modules.events.domain.categories;

import com.evently.common.domain.Entity;
import java.util.UUID;

public class Category extends Entity {
    public Category() {
    }

    private UUID id;
    private String name;
    private boolean isArchived;

    public static Category create(String name) {
        Category category = new Category();
        category.id = UUID.randomUUID();
        category.name = name;
        category.isArchived = false;

        category.raise(new CategoryCreatedDomainEvent(category.id));

        return category;
    }

    public void archive() {
        isArchived = true;

        raise(new CategoryArchivedDomainEvent(id));
    }

    public void changeName(String name) {
        if (this.name.equals(name)) {
            return;
        }

        this.name = name;

        raise(new CategoryNameChangedDomainEvent(id, name));
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isArchived() {
        return isArchived;
    }

    // Setters for JPA
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}