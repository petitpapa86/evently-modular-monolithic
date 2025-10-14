package com.evently.modules.users.domain.users;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
    Optional<User> get(UUID id);

    Optional<User> getByIdentityId(String identityId);

    Optional<User> getByEmail(String email);

    boolean isEmailUnique(String email);

    void insert(User user);

    void update(User user);
}