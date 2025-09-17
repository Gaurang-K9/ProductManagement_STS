package com.demo.model.Product;

public class ProductConverter {

    public static Product toProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProduct(productDTO.getProduct());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        return product;
    }
}
