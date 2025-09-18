package com.demo.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long product_id;
    private String product;
    private String category;
    private Double price;
}
