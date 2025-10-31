package com.demo.model.user;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    public static User toUser(UserDTO userDTO){

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setWishlist(new ArrayList<>());
        user.setReviews(new ArrayList<>());
        user.setAddresses(new ArrayList<>());
        return user;
    }

    public static UserResponseDTO toUserResponseDTO(User user){

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        List<String> wishlist = new ArrayList<>();
        List<String> reviews = new ArrayList<>();
        user.getReviews().forEach(review -> reviews.add(review.getReview()));
        user.getWishlist().forEach( product -> wishlist.add(product.getProductName()));
        userResponseDTO.setWishlist(wishlist);
        userResponseDTO.setReviews(reviews);
        return userResponseDTO;
    }

    public static List<UserResponseDTO> toUserResponseList(List<User> users){

        List<UserResponseDTO> dtosList = new ArrayList<>();
        users.forEach(user -> dtosList.add(toUserResponseDTO(user)));
        return dtosList;
    }

    public static UserCartDTO toUserCartDTO(User user){
        UserCartDTO userCartDTO = new UserCartDTO();
        userCartDTO.setUsername(user.getUsername());
        userCartDTO.setEmail(user.getEmail());
        return userCartDTO;
    }
}
