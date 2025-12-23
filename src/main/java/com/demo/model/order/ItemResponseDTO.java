package com.demo.model.order;

import com.demo.model.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDTO {

    private String orderCode;
    private ProductResponseDTO product;
    private Integer quantity;
}
