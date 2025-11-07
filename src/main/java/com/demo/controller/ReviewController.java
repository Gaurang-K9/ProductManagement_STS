package com.demo.controller;

import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewResponseDTO;
import com.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/common")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> findAllReviews(@RequestParam (required = false) Long productId){
        List<Review> reviewList;
        if(productId != null){
            reviewList = reviewService.findReviewsByProductId(productId);
        }
        else {
            reviewList = reviewService.findAllReviews();
        }
        List<ReviewResponseDTO> reviews = ReviewConverter.toReviewResponseList(reviewList);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> findReviewById(@PathVariable Long id){
        Review review = reviewService.findReviewById(id);
        ReviewResponseDTO reviewResponseDTO = ReviewConverter.toReviewResponseDTO(review);
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDTO);
    }

    @PostMapping("/reviews")
    public ResponseEntity<Map<String, String>> addReview(@RequestBody Review review){
        String response = reviewService.addReview(review);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Map<String, String>> deleteReviewById(@PathVariable Long id){
        String response = reviewService.deleteReviewById(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
