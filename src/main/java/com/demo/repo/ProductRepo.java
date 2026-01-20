package com.demo.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.model.product.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory(String category);

    Page<Product> findByCategory(String category, Pageable pageable);

	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<Product> findDistinctByReviews_StarGreaterThan(Short star);

    List<Product> findByOwner_UserId(Long ownerId);

    Page<Product> findByOwner_UserId(Long ownerId, Pageable pageable);

    List<Product> findByOwner_Username(String username);

    Page<Product> findByOwner_Username(String username, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p HAVING COALESCE(AVG(r.star), 0) > :minAvgStar")
    List<Product> findByAverageStarGreaterThan(Short minAvgStar);

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p HAVING COALESCE(AVG(r.star), 0) > :minAvgStar")
    Page<Product> findByAverageStarGreaterThan(Short minAvgStar, Pageable pageable);
}
