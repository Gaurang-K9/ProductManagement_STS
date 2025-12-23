package com.demo.model.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {

    private Long productId;
    private String productName;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer stockThreshold;
}
