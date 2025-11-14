package com.demo.model.cart;

import com.demo.model.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long itemId;
    private ProductResponseDTO product;
    private Integer quantity;
}
