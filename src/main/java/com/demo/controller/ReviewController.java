package com.demo.controller;

import com.demo.model.review.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.ReviewService;
import com.demo.shared.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/all")
    public PageResponse<ReviewResponseDTO> findAllReviews(
            @PageableDefault(sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam (required = false) Long productId) {
        if(productId != null){
            return PageResponse.fromPage(reviewService.findReviewsByProductId(productId, pageable).map(
                    ReviewConverter::toReviewResponseDTO
            ));
        }
        return PageResponse.fromPage(reviewService.findAllReviews(pageable).map(
                ReviewConverter::toReviewResponseDTO
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> findReviewById(@PathVariable Long id){
        Review review = reviewService.findReviewById(id);
        ReviewResponseDTO reviewResponseDTO = ReviewConverter.toReviewResponseDTO(review);
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addReview(@RequestBody Review review){
        String response = reviewService.addReview(review);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Map<String, String>> deleteReviewById(@PathVariable Long id){
        String response = reviewService.deleteReviewById(id);
        Map<String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/my")
    public PageResponse<UserReviewResponseDTO> findUserReviews(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "reviewId", direction = Sort.Direction.ASC) Pageable pageable){
        return PageResponse.fromPage(reviewService.findUserReviews(userPrincipal, pageable).map(
                ReviewConverter::toUserReviewResponseDTO
        ));
    }

    @PostMapping("/post")
    public ResponseEntity<Map <String, String>> addReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ReviewDTO reviewDTO){
        String response = reviewService.addUserReview(userPrincipal, reviewDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/update/{reviewid}")
    public ResponseEntity<Map <String, String>> updateReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewid, @RequestBody ReviewDTO reviewDTO){
        String response = reviewService.updateUserReview(userPrincipal, reviewid, reviewDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/delete/{reviewid}")
    public ResponseEntity<Map <String, String>> updateReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewid) {
        String response = reviewService.deleteUserReview(userPrincipal, reviewid);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
