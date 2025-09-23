package com.demo.model.user;

import com.demo.model.Product.Product;
import com.demo.model.review.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String username;
    private String email;
    private List<String> wishlist;
    private List<String> reviews;
}
