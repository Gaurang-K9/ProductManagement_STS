package com.demo.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewResponseDTO {

    private Long reviewId;
    private String username;
    private String productName;
    private String review;
    private Short rating;
    private LocalDateTime createdAt;
}
