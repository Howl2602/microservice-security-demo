package com.dien.inventoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dien.inventoryservice.entity.CustomerInventory;
import com.dien.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")

public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<CustomerInventory>getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @PostMapping
    public CustomerInventory createInventory(@RequestBody CustomerInventory inventory){
        return inventoryService.createInventory(inventory);
    }
}
