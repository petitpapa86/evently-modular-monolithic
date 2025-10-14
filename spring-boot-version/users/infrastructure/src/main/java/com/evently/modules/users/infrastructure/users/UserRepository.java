package com.evently.modules.users.infrastructure.users;

import com.evently.modules.users.domain.users.IUserRepository;
import com.evently.modules.users.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, IUserRepository {
    @Override
    default Optional<User> get(UUID id) {
        return findById(id).map(UserEntity::toDomain);
    }

    @Override
    default Optional<User> getByIdentityId(String identityId) {
        return findByIdentityId(identityId).map(UserEntity::toDomain);
    }

    Optional<UserEntity> findByIdentityId(String identityId);

    @Override
    default Optional<User> getByEmail(String email) {
        return findByEmail(email).map(UserEntity::toDomain);
    }

    Optional<UserEntity> findByEmail(String email);

    @Override
    default boolean isEmailUnique(String email) {
        return !existsByEmail(email);
    }

    boolean existsByEmail(String email);

    @Override
    default void insert(User user) {
        save(UserEntity.fromDomain(user));
    }

    @Override
    default void update(User user) {
        save(UserEntity.fromDomain(user));
    }
}