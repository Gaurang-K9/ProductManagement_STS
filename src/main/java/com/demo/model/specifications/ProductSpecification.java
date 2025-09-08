package com.demo.model.specifications;

import com.demo.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasCategory(String category){
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }
}
