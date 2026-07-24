package com.dien.inventoryservice.event;

import java.time.Instant;

public record OrderCreatedEvent(
        Long orderId,
        String customerName,
        String productName,
        Integer quantity,
        Double totalPrice,
        Instant createdAt) {
}
