package com.demo.model.user;

import com.demo.model.address.Address;
import com.demo.model.address.AddressResponseDTO;
import com.demo.model.company.CompanyConverter;
import com.demo.model.company.CompanyDTO;
import com.demo.model.product.Product;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductResponseDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

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

        /*List<UserResponseDTO> dtosList = new ArrayList<>();
        users.forEach(user -> dtosList.add(toUserResponseDTO(user)));
        return dtosList;*/
        return users.stream().map(UserConverter::toUserResponseDTO).toList();
    }

    public static SimpleUserDTO toSimpleUserDTO(User user){

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
        simpleUserDTO.setUsername(user.getUsername());
        simpleUserDTO.setFirstName(user.getFirstName());
        simpleUserDTO.setLastName(user.getLastName());
        simpleUserDTO.setMobileNumber(user.getMobileNumber());
        simpleUserDTO.setEmail(user.getEmail());
        return simpleUserDTO;
    }

    public static UserProfileDTO toUserProfileDTO(User user){

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setFirstName(user.getFirstName());
        userProfileDTO.setLastName(user.getLastName());
        userProfileDTO.setMobileNumber(user.getMobileNumber());
        userProfileDTO.setEmail(user.getEmail());
        List<AddressResponseDTO> addresses = toAddressResponseDTOList(user.getAddresses());
        userProfileDTO.setAddresses(addresses);
        return userProfileDTO;
    }

    public static ProductOwnerDTO toProductOwnerDTO(User user, List<Product> products){

        ProductOwnerDTO productOwnerDTO = new ProductOwnerDTO();
        productOwnerDTO.setUsername(user.getUsername());
        productOwnerDTO.setFirstName(user.getFirstName());
        productOwnerDTO.setLastName(user.getLastName());
        productOwnerDTO.setMobileNumber(user.getMobileNumber());
        productOwnerDTO.setEmail(user.getEmail());
        List<ProductResponseDTO> ownerProducts = ProductConverter.toProductResponseList(products);
        productOwnerDTO.setProducts(ownerProducts);
        CompanyDTO companyDTO = CompanyConverter.toCompanyDTO(user.getCompany());
        productOwnerDTO.setCompany(companyDTO);
        return productOwnerDTO;
    }

    public static List<AddressResponseDTO> toAddressResponseDTOList(List<Address> addressList) {

        return IntStream.range(0, addressList.size())
                        .mapToObj(i -> {Address address = addressList.get(i);
                        return new AddressResponseDTO(i,
                                address.getStreetAddress(),
                                address.getCity(),
                                address.getState(),
                                address.getPincode());
                        })
                        .toList();
    }
}
