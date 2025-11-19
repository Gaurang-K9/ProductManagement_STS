package com.demo.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {

    private Long productId;
    private String productName;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer stockThreshold;
}
