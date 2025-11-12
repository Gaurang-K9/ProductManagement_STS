package com.demo.model.order;

import com.demo.model.Product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO {

    private String orderCode;
    private ProductResponseDTO product;
    private Integer quantity;
}
