package com.dien.orderservice.service;

import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dien.orderservice.dto.InventoryResponse;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository,
                        RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public List<CustomerOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public CustomerOrder createOrder(CustomerOrder order){

        InventoryResponse inventory =
                checkInventory(order.getProductName());

        if(inventory == null){
            throw new RuntimeException("Product not found");
        }

        if(inventory.getStock() < order.getQuantity()){
            throw new RuntimeException("Out of stock");
        }

        return orderRepository.save(order);
    }

    public InventoryResponse checkInventory(String productName) {

        String url =
                "http://localhost:8081/inventory/" + productName;

        return restTemplate.getForObject(
                url,
                InventoryResponse.class
        );
    }

}