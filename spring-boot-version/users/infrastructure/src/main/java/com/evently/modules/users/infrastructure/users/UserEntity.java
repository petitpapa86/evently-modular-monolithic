package com.evently.modules.users.infrastructure.users;

import com.evently.modules.users.domain.users.Role;
import com.evently.modules.users.domain.users.User;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "UsersUserEntity")
@Table(name = "users", schema = "users")
public class UserEntity {
    @Id
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String identityId;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(UUID id, String email, String firstName, String lastName, String identityId, List<String> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityId = identityId;
        this.roles = roles;
    }

    public User toDomain() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIdentityId(identityId);
        user.setRoles(roles.stream().map(Role::new).collect(Collectors.toList()));
        return user;
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getIdentityId(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}