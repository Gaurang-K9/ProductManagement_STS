package com.demo.controller;

import com.demo.model.review.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.ReviewService;
import com.demo.shared.ApiResponse;
import com.demo.shared.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponseDTO>>> findAllReviews(
            @PageableDefault(sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam (required = false) Long productId) {
        if(productId != null){
            var response = PageResponse
                    .fromPage(reviewService.findReviewsByProductId(productId, pageable)
                    .map(ReviewConverter::toReviewResponseDTO));
            String message = "Reviews fetched for Product ID: "+productId;
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
        }
        var response = PageResponse
                .fromPage(reviewService.findAllReviews(pageable)
                .map(ReviewConverter::toReviewResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> findReviewById(@PathVariable Long id){
        Review review = reviewService.findReviewById(id);
        ReviewResponseDTO reviewResponseDTO = ReviewConverter.toReviewResponseDTO(review);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(reviewResponseDTO));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReviewById(@PathVariable Long id){
        String response = reviewService.deleteReviewById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<UserReviewResponseDTO>>> findUserReviews(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "reviewId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse
                .fromPage(reviewService.findUserReviews(userPrincipal, pageable)
                .map(ReviewConverter::toUserReviewResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<String>> addReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ReviewDTO reviewDTO){
        String response = reviewService.addUserReview(userPrincipal, reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PatchMapping("/update/{reviewid}")
    public ResponseEntity<ApiResponse<String>> updateReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewid, @RequestBody ReviewDTO reviewDTO){
        String response = reviewService.updateUserReview(userPrincipal, reviewid, reviewDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @DeleteMapping("/delete/{reviewid}")
    public ResponseEntity<ApiResponse<String>> updateReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewid) {
        String response = reviewService.deleteUserReview(userPrincipal, reviewid);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
