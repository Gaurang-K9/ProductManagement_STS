package com.demo.model.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewResponseDTO {

    private Long reviewId;
    private String username;
    private String productName;
    private String review;
    private Short star;
}
