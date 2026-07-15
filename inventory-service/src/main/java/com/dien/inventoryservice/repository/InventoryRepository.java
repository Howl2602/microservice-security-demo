package com.dien.inventoryservice.repository;

import com.dien.inventoryservice.entity.CustomerInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<CustomerInventory, Long> {

}
