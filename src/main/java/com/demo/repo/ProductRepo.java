package com.demo.repo;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.model.product.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    String ratingMoreThanQuery = "SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p HAVING COALESCE(AVG(r.star), 0) > :minAvgStar";

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByOwner_UserId(Long ownerId, Pageable pageable);

    Page<Product> findByOwner_Username(String username, Pageable pageable);

    @Query(value = ratingMoreThanQuery)
    Page<Product> findByAverageStarGreaterThan(@Param("minAvgStar") Short minAvgStar, Pageable pageable);
}
