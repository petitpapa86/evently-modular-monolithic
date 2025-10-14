package com.evently.modules.ticketing.domain.payments;

import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepository {

    Optional<Payment> get(UUID paymentId);

    void insert(Payment payment);
}