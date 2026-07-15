package com.dien.inventoryservice.service;

import com.dien.inventoryservice.entity.CustomerInventory;
import com.dien.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepostiory) {
        this.inventoryRepository =inventoryRepostiory;
    }

    public List<CustomerInventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public CustomerInventory createInventory(CustomerInventory inventory) {
        return inventoryRepository.save(inventory);
    }
}