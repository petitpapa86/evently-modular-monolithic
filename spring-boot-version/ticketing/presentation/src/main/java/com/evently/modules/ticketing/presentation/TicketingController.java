package com.evently.modules.ticketing.presentation;

import com.evently.common.application.IMediator;
import com.evently.common.domain.Result;
import com.evently.modules.ticketing.application.orders.additemtocart.AddItemToCartCommand;
import com.evently.modules.ticketing.application.orders.processpayment.ProcessPaymentCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticketing")
public class TicketingController {
    private final IMediator mediator;

    public TicketingController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/cart/items")
    public ResponseEntity<String> addItemToCart(@RequestBody AddItemToCartRequest request) {
        AddItemToCartCommand command = new AddItemToCartCommand(
                request.customerId(),
                request.ticketTypeId(),
                BigDecimal.valueOf(request.quantity())
        );

        Result<Void> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("Item added to cart");
        } else {
            return ResponseEntity.badRequest().body("Error adding item to cart");
        }
    }

    @PostMapping("/orders/{orderId}/payment")
    public ResponseEntity<String> processPayment(@PathVariable UUID orderId, @RequestBody ProcessPaymentRequest request) {
        ProcessPaymentCommand command = new ProcessPaymentCommand(
                orderId,
                request.transactionId(),
                request.amount(),
                request.currency()
        );

        Result<Void> result = mediator.send(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok("Payment processed successfully");
        } else {
            return ResponseEntity.badRequest().body("Error processing payment");
        }
    }

    public record AddItemToCartRequest(
            UUID customerId,
            UUID ticketTypeId,
            int quantity,
            BigDecimal price) {
    }

    public record ProcessPaymentRequest(
            BigDecimal amount,
            String currency,
            String transactionId) {
    }
}