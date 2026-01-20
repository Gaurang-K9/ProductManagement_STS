package com.demo.model.review;

import java.util.ArrayList;
import java.util.List;

public class ReviewConverter {

    public static Review toReview(ReviewDTO reviewDTO){
        Review review = new Review();
        review.setReview(reviewDTO.getReview());
        review.setStar(reviewDTO.getStar());
        return review;
    }

    public static ReviewResponseDTO toReviewResponseDTO(Review review){
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setReviewId(review.getReviewId());
        reviewResponseDTO.setUsername(review.getUser().getUsername());
        reviewResponseDTO.setReview(review.getReview());
        reviewResponseDTO.setStar(review.getStar());
        return reviewResponseDTO;
    }

    public static List<ReviewResponseDTO> toReviewResponseList(List<Review> reviews){
        List<ReviewResponseDTO> dtosList = new ArrayList<>();
        reviews.forEach(review -> dtosList.add(toReviewResponseDTO(review)));
        return dtosList;
    }

    public static UserReviewResponseDTO toUserReviewResponseDTO(Review review){
        UserReviewResponseDTO userReviewResponseDTO = new UserReviewResponseDTO();
        userReviewResponseDTO.setReviewId(review.getReviewId());
        userReviewResponseDTO.setUsername(review.getUser().getUsername());
        userReviewResponseDTO.setProductName(review.getProductReview().getProductName());
        userReviewResponseDTO.setReview(review.getReview());
        userReviewResponseDTO.setStar(review.getStar());
        return userReviewResponseDTO;
    }

    public static List<UserReviewResponseDTO> toUserReviewsList(List<Review> reviews){
        List<UserReviewResponseDTO> dtosList = new ArrayList<>();
        reviews.forEach(review -> dtosList.add(toUserReviewResponseDTO(review)));
        return dtosList;
    }
}
