package com.evently.modules.events.infrastructure.categories;

import com.evently.modules.events.domain.categories.Category;
import com.evently.modules.events.domain.categories.ICategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID>, ICategoryRepository {
    @Override
    default Optional<Category> get(UUID id) {
        return findById(id).map(CategoryEntity::toDomain);
    }

    @Override
    default void insert(Category category) {
        save(CategoryEntity.fromDomain(category));
    }
}