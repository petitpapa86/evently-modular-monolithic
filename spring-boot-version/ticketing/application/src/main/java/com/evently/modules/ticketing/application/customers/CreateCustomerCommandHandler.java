package com.evently.modules.ticketing.application.customers;

import com.evently.common.application.ICommandHandler;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.domain.customers.Customer;
import com.evently.modules.ticketing.domain.customers.ICustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateCustomerCommandHandler implements ICommandHandler<CreateCustomerCommand, Void> {

    private final ICustomerRepository customerRepository;

    public CreateCustomerCommandHandler(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Result<Void> handle(CreateCustomerCommand request) {
        // Check if customer already exists
        if (customerRepository.existsById(request.userId())) {
            return Result.success(null); // Customer already exists, nothing to do
        }

        Customer customer = Customer.create(
            request.userId(),
            request.email(),
            request.firstName(),
            request.lastName()
        );

        customerRepository.save(customer);

        return Result.success(null);
    }
}