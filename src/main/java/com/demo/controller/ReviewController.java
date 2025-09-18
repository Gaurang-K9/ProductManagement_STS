package com.demo.controller;

import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewResponseDTO;
import com.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/common")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> findAllReviews(){
        List<Review> list = reviewService.findAllReviews();
        List<ReviewResponseDTO> reviews = new ArrayList<>();
        list.forEach(single -> reviews.add(ReviewConverter.toReviewResponseDTO(single)));
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> findReviewById(@PathVariable Long id){
        Optional<Review> optional = reviewService.findReviewById(id);
        return optional.map(review -> new ResponseEntity<>(ReviewConverter.toReviewResponseDTO(review), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new ReviewResponseDTO(), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/review")
    public ResponseEntity<String> addReview(@RequestBody Review review){
        String response = reviewService.addReview(review);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long id){
        String response = reviewService.deleteReviewById(id);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
