package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.review.Review;
import com.demo.model.review.ReviewDTO;
import com.demo.repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    public List<Review> findAllReviews(){
        return reviewRepo.findAll();
    }

    public Review findReviewById(Long id){
        return reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Review.class, "reviewId", id));
    }

    public String addReview(Review review){
        reviewRepo.save(review);
        return "Review added successfully";
    }

    public String updateReview(Review review){
        reviewRepo.save(review);
        return "Review Updated Successfully";
    }

    public String updateReview(Long id, ReviewDTO reviewDTO){
        Review oldReview = reviewRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(Review.class, "reviewId", id));
        oldReview.setReview(reviewDTO.getReview());
        oldReview.setStar(reviewDTO.getStar());
        reviewRepo.save(oldReview);
        return "Review updated successfully";
    }

    public String deleteReviewById(Long id){
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Review.class, "reviewId", id));
        reviewRepo.delete(review);
        return "Review deleted successfully";
    }

    public List<Review> findReviewsByProductId(Long productId) {
        return reviewRepo.findByProductReview_ProductId(productId);
    }
}
