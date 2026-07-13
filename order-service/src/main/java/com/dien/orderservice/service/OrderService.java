package com.dien.orderservice.service;

import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public CustomerOrder createOrder(CustomerOrder order) {
        return orderRepository.save(order);
    }
}