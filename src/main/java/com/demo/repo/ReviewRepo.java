package com.demo.repo;

import com.demo.model.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    Page<Review> findByProductReview_ProductId(Long productId, Pageable pageable);

    Page<Review> findByUser_UserId(Long userId, Pageable pageable);

    Optional<Review> findByReviewIdAndUserUserId(Long reviewId, Long userId);   //For Resource Ownership
}
