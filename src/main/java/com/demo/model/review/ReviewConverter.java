package com.demo.model.review;

public class ReviewConverter {

    public static Review toReview(ReviewDTO reviewDTO){
        Review review = new Review();
        review.setReview(reviewDTO.getReview());
        return review;
    }

    public static ReviewResponseDTO toReviewResponseDTO(Review review){
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setReview_id(review.getReview_id());
        reviewResponseDTO.setUsername(review.getUser().getUsername());
        reviewResponseDTO.setReview(review.getReview());
        return reviewResponseDTO;
    }
}
