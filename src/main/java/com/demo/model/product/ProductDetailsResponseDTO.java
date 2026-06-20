package com.demo.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponseDTO {

    private Long productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private Double averageRating;
    private Integer reviewCount;
}
