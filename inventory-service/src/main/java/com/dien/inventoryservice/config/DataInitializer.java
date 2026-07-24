package com.dien.inventoryservice.config;

import com.dien.inventoryservice.entity.CustomerInventory;
import com.dien.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedInventory(InventoryRepository repository) {
        return args -> {
            createIfMissing(repository, "Laptop", 10);
            createIfMissing(repository, "Keyboard", 50);
            createIfMissing(repository, "Mouse", 100);
        };
    }

    private void createIfMissing(
            InventoryRepository repository,
            String productName,
            int stock) {
        if (repository.findByProductNameIgnoreCase(productName).isEmpty()) {
            repository.save(new CustomerInventory(null, productName, stock));
        }
    }
}
