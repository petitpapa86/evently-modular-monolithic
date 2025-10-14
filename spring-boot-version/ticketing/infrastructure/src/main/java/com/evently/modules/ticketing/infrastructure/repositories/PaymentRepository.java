package com.evently.modules.ticketing.infrastructure.repositories;

import com.evently.modules.ticketing.domain.payments.IPaymentRepository;
import com.evently.modules.ticketing.domain.payments.Payment;
import com.evently.modules.ticketing.infrastructure.entities.PaymentEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepository implements IPaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    public PaymentRepository(JpaPaymentRepository jpaPaymentRepository) {
        this.jpaPaymentRepository = jpaPaymentRepository;
    }

    @Override
    public Optional<Payment> get(UUID id) {
        // Simplified for now - would need proper domain reconstruction
        return Optional.empty();
    }

    @Override
    public void insert(Payment payment) {
        PaymentEntity entity = new PaymentEntity(
                payment.getId(),
                payment.getOrderId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getAmountRefunded(),
                payment.getCreatedAtUtc(),
                payment.getRefundedAtUtc()
        );
        jpaPaymentRepository.save(entity);
    }

    public void update(Payment payment) {
        Optional<PaymentEntity> existingEntity = jpaPaymentRepository.findById(payment.getId());
        if (existingEntity.isPresent()) {
            PaymentEntity entity = existingEntity.get();
            entity.setAmountRefunded(payment.getAmountRefunded());
            entity.setRefundedAtUtc(payment.getRefundedAtUtc());
            jpaPaymentRepository.save(entity);
        }
    }
}