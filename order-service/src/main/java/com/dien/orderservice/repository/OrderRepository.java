package com.dien.orderservice.repository;

import com.dien.orderservice.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomerNameOrderByIdAsc(String customerName);
}
