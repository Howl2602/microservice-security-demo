package com.dien.inventoryservice.messaging;

import com.dien.inventoryservice.event.OrderCreatedEvent;
import com.dien.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderCreatedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;

    public OrderCreatedListener(ObjectMapper objectMapper, InventoryService inventoryService) {
        this.objectMapper = objectMapper;
        this.inventoryService = inventoryService;
    }

    @RabbitListener(queues = "${microshield.rabbit.inventory-queue}")
    public void handleOrderCreated(Message message) throws IOException {
        OrderCreatedEvent event = objectMapper.readValue(
                message.getBody(),
                OrderCreatedEvent.class);

        inventoryService.decreaseStock(event.productName(), event.quantity());
        LOGGER.info(
                "Inventory updated for order {}: {} x {}",
                event.orderId(),
                event.quantity(),
                event.productName());
    }
}
