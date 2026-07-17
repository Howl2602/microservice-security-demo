package com.dien.inventoryservice.repository;

import com.dien.inventoryservice.entity.CustomerInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<CustomerInventory, Long> {
    Optional<CustomerInventory> findByProductName(String productName);

}
