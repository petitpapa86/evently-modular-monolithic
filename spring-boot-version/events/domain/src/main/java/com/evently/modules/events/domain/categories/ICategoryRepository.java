package com.evently.modules.events.domain.categories;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryRepository {
    Optional<Category> get(UUID id);

    void insert(Category category);
}