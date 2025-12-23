package com.demo.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private String imageUrl;
}
