package com.demo.controller;

import com.demo.model.Review;
import com.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/common")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews")
    public List<Review> findAllReviews(){
        return reviewService.findAllReviews();
    }

    @GetMapping("/reviews/{id}")
    public Review findReviewById(@PathVariable Long id){
        Optional<Review> optional = reviewService.findReviewById(id);
        return optional.orElseGet(Review::new);
    }

    @PostMapping("/review")
    public String addReview(@RequestBody Review review){
        return reviewService.addReview(review);
    }

    @DeleteMapping("/review/{id}")
    public String deleteReviewById(@PathVariable Long id){
        return reviewService.deleteReviewById(id);
    }
}
