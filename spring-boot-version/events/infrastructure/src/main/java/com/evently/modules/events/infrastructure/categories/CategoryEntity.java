package com.evently.modules.events.infrastructure.categories;

import com.evently.modules.events.domain.categories.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    private UUID id;
    private String name;
    private boolean isArchived;

    public CategoryEntity() {
    }

    public CategoryEntity(UUID id, String name, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
    }

    public Category toDomain() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setArchived(isArchived);
        return category;
    }

    public static CategoryEntity fromDomain(Category category) {
        return new CategoryEntity(category.getId(), category.getName(), category.isArchived());
    }

    // getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}