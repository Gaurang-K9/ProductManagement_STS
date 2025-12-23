package com.demo.model.cart;

import com.demo.model.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long itemId;
    private ProductResponseDTO product;
    private Integer quantity;
}
