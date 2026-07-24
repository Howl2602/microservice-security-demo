package com.dien.inventoryservice.controller;

import com.dien.inventoryservice.entity.CustomerInventory;
import com.dien.inventoryservice.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<CustomerInventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PostMapping
    public CustomerInventory createInventory(@RequestBody CustomerInventory inventory) {
        return inventoryService.createInventory(inventory);
    }

    @GetMapping("/{productName}")
    public CustomerInventory getInventoryByProductName(@PathVariable String productName) {
        return inventoryService.getInventoryByProductName(productName);
    }
}
