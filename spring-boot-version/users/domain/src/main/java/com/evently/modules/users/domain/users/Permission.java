package com.evently.modules.users.domain.users;

public class Permission {
    public static final Permission GET_USER = new Permission("users:read");
    public static final Permission MODIFY_USER = new Permission("users:update");
    public static final Permission GET_EVENTS = new Permission("events:read");
    public static final Permission SEARCH_EVENTS = new Permission("events:search");
    public static final Permission MODIFY_EVENTS = new Permission("events:update");
    public static final Permission GET_TICKET_TYPES = new Permission("ticket-types:read");
    public static final Permission MODIFY_TICKET_TYPES = new Permission("ticket-types:update");
    public static final Permission GET_CATEGORIES = new Permission("categories:read");
    public static final Permission MODIFY_CATEGORIES = new Permission("categories:update");
    public static final Permission GET_CART = new Permission("carts:read");
    public static final Permission ADD_TO_CART = new Permission("carts:add");
    public static final Permission REMOVE_FROM_CART = new Permission("carts:remove");
    public static final Permission GET_ORDERS = new Permission("orders:read");
    public static final Permission CREATE_ORDER = new Permission("orders:create");
    public static final Permission GET_TICKETS = new Permission("tickets:read");
    public static final Permission CHECK_IN_TICKET = new Permission("tickets:check-in");
    public static final Permission GET_EVENT_STATISTICS = new Permission("event-statistics:read");

    private final String code;

    public Permission(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Permission permission = (Permission) obj;
        return code != null ? code.equals(permission.code) : permission.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return code;
    }
}