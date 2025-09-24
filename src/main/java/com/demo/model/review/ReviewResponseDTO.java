package com.demo.model.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long reviewId;
    private String username;
    private String review;
    private Short star;
}
