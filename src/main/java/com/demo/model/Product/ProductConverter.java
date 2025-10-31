package com.demo.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductConverter {

    public static Product toProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        return product;
    }

    public static ProductResponseDTO toProductResponseDTO(Product product){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProductId(product.getProductId());
        productResponseDTO.setProductName(product.getProductName());
        productResponseDTO.setCategory(product.getCategory());
        productResponseDTO.setPrice(product.getPrice());
        return productResponseDTO;
    }

    public static List<ProductResponseDTO> toProductResponseList(List<Product> productList){
        List<ProductResponseDTO> dtoslist = new ArrayList<>();
        productList.forEach(product -> dtoslist.add(toProductResponseDTO(product)));
        return dtoslist;
    }
}
