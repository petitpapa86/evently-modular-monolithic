package com.evently.modules.events.domain.categories;

import com.evently.common.domain.Error;
import java.util.UUID;

public class CategoryErrors {
    public static Error notFound(UUID categoryId) {
        return Error.notFound("Categories.NotFound", "The category with the identifier " + categoryId + " was not found");
    }

    public static final Error ALREADY_ARCHIVED = Error.problem("Categories.AlreadyArchived", "The category was already archived");
}