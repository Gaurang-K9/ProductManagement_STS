package com.demo.model.user;

import java.util.ArrayList;

public class UserConverter {

    public static User toUser(UserDTO userDTO){

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setWishlist(new ArrayList<>());
        user.setReviews(new ArrayList<>());
        return user;
    }

    public static UserResponseDTO toUserResponseDTO(User user){

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setWishlist(user.getWishlist());
        userResponseDTO.setReviews(user.getReviews());
        return userResponseDTO;
    }
}
