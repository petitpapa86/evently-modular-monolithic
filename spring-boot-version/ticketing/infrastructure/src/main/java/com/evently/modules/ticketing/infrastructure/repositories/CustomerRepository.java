package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.customers.Customer;
import com.evently.modules.ticketing.domain.customers.ICustomerRepository;
import com.evently.modules.ticketing.infrastructure.entities.CustomerEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepository implements ICustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    public CustomerRepository(JpaCustomerRepository jpaCustomerRepository) {
        this.jpaCustomerRepository = jpaCustomerRepository;
    }

    @Override
    public Optional<Customer> get(UUID id) {
        return jpaCustomerRepository.findById(id)
                .map(entity -> Customer.create(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getFirstName(),
                        entity.getLastName()
                ));
    }

    @Override
    public void insert(Customer customer) {
        CustomerEntity entity = new CustomerEntity(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
        jpaCustomerRepository.save(entity);
    }

    @Override
    public boolean existsById(UUID customerId) {
        return jpaCustomerRepository.existsById(customerId);
    }

    @Override
    public void save(Customer customer) {
        // For now, just insert if not exists. In a real implementation, you might need to handle updates differently
        if (!existsById(customer.getId())) {
            insert(customer);
        }
    }
}