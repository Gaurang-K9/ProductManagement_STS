package com.demo.model.Product;

public class ProductConverter {

    public static Product toProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProduct(productDTO.getProduct());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        return product;
    }

    public static ProductResponseDTO toProductResponseDTO(Product product){
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProduct_id(product.getProduct_id());
        productResponseDTO.setProduct(product.getProduct());
        productResponseDTO.setCategory(product.getCategory());
        productResponseDTO.setPrice(product.getPrice());
        return productResponseDTO;
    }
}
