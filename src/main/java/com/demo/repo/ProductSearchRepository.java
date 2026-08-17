package com.demo.repo;

import com.demo.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductSearchRepository {

    Page<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Short rating, Pageable pageable);
}
