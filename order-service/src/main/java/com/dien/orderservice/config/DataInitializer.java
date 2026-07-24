package com.dien.orderservice.config;

import com.dien.orderservice.entity.CustomerOrder;
import com.dien.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedOrders(OrderRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new CustomerOrder(null, "userA", "Laptop", 1, 1500.0));
                repository.save(new CustomerOrder(null, "userB", "Keyboard", 2, 100.0));
                repository.save(new CustomerOrder(null, "userB", "Mouse", 1, 25.0));
            }
        };
    }
}
