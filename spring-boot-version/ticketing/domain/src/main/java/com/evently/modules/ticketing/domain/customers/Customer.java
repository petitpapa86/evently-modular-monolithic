package com.evently.modules.ticketing.domain.customers;

import com.evently.common.domain.Entity;

import java.util.UUID;

public class Customer extends Entity {

    private Customer() {
    }

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;

    public static Customer create(UUID id, String email, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.id = id;
        customer.email = email;
        customer.firstName = firstName;
        customer.lastName = lastName;
        return customer;
    }

    public void update(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}