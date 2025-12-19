package com.demo.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.model.product.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory(String category);

	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findDistinctByReviews_StarGreaterThan(Short star);

    List<Product> findByOwner_UserId(Long ownerId);

    List<Product> findByOwner_Username(String username);

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p HAVING COALESCE(AVG(r.star), 0) > :minAvgStar")
    List<Product> findByAverageStarGreaterThan(Short minAvgStar);
}
