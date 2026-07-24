package com.dien.orderservice.service;

import com.dien.orderservice.dto.InventoryResponse;
import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.event.OrderCreatedEvent;
import com.dien.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderEventPublisher eventPublisher;
    private final boolean bolaEnabled;

    public OrderService(
            OrderRepository orderRepository,
            RestTemplate restTemplate,
            OrderEventPublisher eventPublisher,
            @Value("${demo.security.bola-enabled:true}") boolean bolaEnabled) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
        this.bolaEnabled = bolaEnabled;
    }

    public List<CustomerOrder> getOrdersForUser(String username) {
        return orderRepository.findByCustomerNameOrderByIdAsc(username);
    }

    public CustomerOrder getOrderById(Long id, String username) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // Intentionally configurable for the BOLA demonstration.
        if (!bolaEnabled && !order.getCustomerName().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this order");
        }

        return order;
    }

    public CustomerOrder createOrder(CustomerOrder order, String username) {
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }

        InventoryResponse inventory = checkInventory(order.getProductName());

        if (inventory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
        }

        if (inventory.getStock() < order.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Out of stock");
        }

        order.setId(null);
        order.setCustomerName(username);
        CustomerOrder saved = orderRepository.save(order);

        eventPublisher.publish(new OrderCreatedEvent(
                saved.getId(),
                saved.getCustomerName(),
                saved.getProductName(),
                saved.getQuantity(),
                saved.getTotalPrice(),
                Instant.now()));

        return saved;
    }

    public InventoryResponse checkInventory(String productName) {
        String url = UriComponentsBuilder
                .fromUriString("http://INVENTORY-SERVICE/api/inventory/{productName}")
                .buildAndExpand(productName)
                .encode()
                .toUriString();

        try {
            return restTemplate.getForObject(url, InventoryResponse.class);
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Inventory service is unavailable or product does not exist");
        }
    }
}
