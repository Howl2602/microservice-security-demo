package com.dien.inventoryservice.service;

import com.dien.inventoryservice.entity.CustomerInventory;
import com.dien.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<CustomerInventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public CustomerInventory createInventory(CustomerInventory inventory) {
        inventory.setId(null);
        return inventoryRepository.save(inventory);
    }

    public CustomerInventory getInventoryByProductName(String productName) {
        return inventoryRepository.findByProductNameIgnoreCase(productName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Transactional
    public void decreaseStock(String productName, int quantity) {
        CustomerInventory inventory = inventoryRepository.findByProductNameIgnoreCase(productName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (inventory.getStock() < quantity) {
            throw new IllegalStateException("Not enough stock for " + productName);
        }

        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
    }
}
