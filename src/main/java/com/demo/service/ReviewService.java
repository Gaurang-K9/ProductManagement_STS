package com.demo.service;

import com.demo.exception.ForbiddenAccessException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.product.Product;
import com.demo.model.review.*;
import com.demo.model.user.User;
import com.demo.model.user.UserPrincipal;
import com.demo.repo.ProductRepo;
import com.demo.repo.ReviewRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;


    public Page<Review> findAllReviews(Pageable pageable){
        return reviewRepo.findAll(pageable);
    }

    public Page<Review> findReviewsByProductId(Long productId, Pageable pageable) {
        return reviewRepo.findByProductReview_ProductId(productId, pageable);
    }

    public Review findReviewById(Long id){
        return reviewRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Review.class, "reviewId", id));
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

    public Page<Review> findUserReviews(UserPrincipal userPrincipal, Pageable pageable){
        Long userId = userPrincipal.user().getUserId();
        return reviewRepo.findByUser_UserId(userId, pageable);
    }

    public String addUserReview(UserPrincipal userPrincipal, ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProductId();
        Long userId = userPrincipal.user().getUserId();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProductReview(product);
        review.setUser(user);
        reviewRepo.save(review);
        return "Review added for Product: "+product.getProductName()+" successfully";
    }

    public String updateUserReview(UserPrincipal userPrincipal, Long reviewId, ReviewDTO reviewDTO){
        Long userId = userPrincipal.user().getUserId();
        Review review = validateResourceOwnership(reviewId, userId);
        Product product = review.getProductReview();
        review.setReview(reviewDTO.getReview());
        review.setStar(reviewDTO.getStar());
        reviewRepo.save(review);
        return "Review updated for Product: "+product.getProductName()+" successfully";
    }

    public String deleteUserReview(UserPrincipal userPrincipal, Long reviewId){
        Long userId = userPrincipal.user().getUserId();
        Review review = validateResourceOwnership(reviewId, userId);
        Product product = review.getProductReview();
        reviewRepo.delete(review);
        return "Review deleted for Product: "+product.getProductName()+" successfully";
    }

    private Review validateResourceOwnership(Long reviewId, Long userId){
        return reviewRepo.findByReviewIdAndUserUserId(reviewId, userId)
                .orElseThrow(() -> new ForbiddenAccessException("You cannot modify this review"));
    }
}
