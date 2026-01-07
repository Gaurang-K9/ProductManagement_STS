package com.demo.model.user;

import com.demo.model.address.Address;
import com.demo.model.company.CompanyConverter;
import com.demo.model.company.CompanyDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserConverter {

    public static User toUser(UserDTO userDTO){

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setWishlist(new HashSet<>());
        user.setReviews(new ArrayList<>());
        user.setAddresses(new ArrayList<>());
        return user;
    }

    public static UserResponseDTO toUserResponseDTO(User user){

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        Set<String> wishlist = new HashSet<>();
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

    public static SimpleUserDTO toSimpleUserDTO(User user){
        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
        simpleUserDTO.setUsername(user.getUsername());
        simpleUserDTO.setEmail(user.getEmail());
        return simpleUserDTO;
    }

    public static UserProfileDTO  toUserProfileDTO(User user){
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setEmail(user.getEmail());
        List<Address> addresses = user.getAddresses();
        userProfileDTO.setAddresses(addresses);
        return userProfileDTO;
    }

    public static ProductOwnerDTO toProductOwnerDTO(User user, List<String> products){
        ProductOwnerDTO productOwnerDTO = new ProductOwnerDTO();
        productOwnerDTO.setUsername(user.getUsername());
        productOwnerDTO.setEmail(user.getEmail());
        productOwnerDTO.setProducts(products);
        CompanyDTO companyDTO = CompanyConverter.toCompanyDTO(user.getCompany());
        productOwnerDTO.setCompany(companyDTO);
        return productOwnerDTO;
    }
}
