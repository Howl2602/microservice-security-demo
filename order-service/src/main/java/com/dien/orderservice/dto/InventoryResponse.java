package com.dien.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
    private Long id;

    private String productName;

    private Integer stock;

}
