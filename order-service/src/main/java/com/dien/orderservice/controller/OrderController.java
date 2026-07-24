package com.dien.orderservice.controller;

import com.dien.orderservice.dto.InventoryResponse;
import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<CustomerOrder> getOrders(
            @RequestHeader(value = "X-Authenticated-User", required = false) String username) {
        return orderService.getOrdersForUser(requireUsername(username));
    }

    @GetMapping("/{id}")
    public CustomerOrder getOrder(
            @PathVariable Long id,
            @RequestHeader(value = "X-Authenticated-User", required = false) String username) {
        return orderService.getOrderById(id, requireUsername(username));
    }

    @PostMapping
    public CustomerOrder createOrder(
            @RequestBody CustomerOrder order,
            @RequestHeader(value = "X-Authenticated-User", required = false) String username) {
        return orderService.createOrder(order, requireUsername(username));
    }

    @GetMapping("/check/{productName}")
    public InventoryResponse checkInventory(@PathVariable String productName) {
        return orderService.checkInventory(productName);
    }

    private String requireUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Request must pass through API Gateway with a valid JWT");
        }
        return username;
    }
}
