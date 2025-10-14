package com.evently.modules.users.domain.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Role {
    public static final Role ADMINISTRATOR = new Role("Administrator", List.of(
        Permission.GET_USER,
        Permission.MODIFY_USER,
        Permission.GET_EVENTS,
        Permission.SEARCH_EVENTS,
        Permission.MODIFY_EVENTS,
        Permission.GET_TICKET_TYPES,
        Permission.MODIFY_TICKET_TYPES,
        Permission.GET_CATEGORIES,
        Permission.MODIFY_CATEGORIES,
        Permission.GET_CART,
        Permission.ADD_TO_CART,
        Permission.REMOVE_FROM_CART,
        Permission.GET_ORDERS,
        Permission.CREATE_ORDER,
        Permission.GET_TICKETS,
        Permission.CHECK_IN_TICKET,
        Permission.GET_EVENT_STATISTICS
    ));

    public static final Role MEMBER = new Role("Member", List.of(
        Permission.GET_USER,
        Permission.GET_EVENTS,
        Permission.SEARCH_EVENTS,
        Permission.GET_TICKET_TYPES,
        Permission.GET_CATEGORIES,
        Permission.GET_CART,
        Permission.ADD_TO_CART,
        Permission.REMOVE_FROM_CART,
        Permission.GET_ORDERS,
        Permission.CREATE_ORDER,
        Permission.GET_TICKETS
    ));

    private final String name;
    private final List<Permission> permissions;

    public Role(String name, List<Permission> permissions) {
        this.name = name;
        this.permissions = new ArrayList<>(permissions);
    }

    public Role(String name) {
        this.name = name;
        this.permissions = new ArrayList<>();
    }

    // Default constructor for JPA
    protected Role() {
        this.name = null;
        this.permissions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Permission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return name != null ? name.equals(role.name) : role.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}