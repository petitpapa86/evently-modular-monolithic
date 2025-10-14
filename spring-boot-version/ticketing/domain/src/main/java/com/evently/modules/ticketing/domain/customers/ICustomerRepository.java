package com.evently.modules.ticketing.domain.customers;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository {

    Optional<Customer> get(UUID customerId);

    void insert(Customer customer);
}