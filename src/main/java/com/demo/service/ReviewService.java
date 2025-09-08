package com.demo.service;

import com.demo.model.Review;
import com.demo.repo.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    public List<Review> findAllReviews(){
        return reviewRepo.findAll();
    }

    public Optional<Review> findReviewById(Long id){
        return reviewRepo.findById(id);
    }

    public String addReview(Review review){
        reviewRepo.save(review);
        return "Review added successfully";
    }

    public String deleteReviewById(long id){
        reviewRepo.deleteById(id);
        return "Review deleted successfully";
    }
}
