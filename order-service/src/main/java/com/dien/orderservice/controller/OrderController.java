package com.dien.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.dien.orderservice.dto.InventoryResponse;

import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<CustomerOrder> getAllOrders(){
        return orderService.getAllOrders();
    }

    @PostMapping
    public CustomerOrder createOrder(@RequestBody CustomerOrder order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/check/{productName}")
    public InventoryResponse checkInventory(
            @PathVariable String productName){

        return orderService.checkInventory(productName);
    }

}
