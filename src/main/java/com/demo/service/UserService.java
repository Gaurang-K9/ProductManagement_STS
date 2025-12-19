package com.demo.service;

import com.demo.exception.ConflictResourceException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.address.Address;
import com.demo.model.product.Product;
import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.*;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public User findUserById(Long id){
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
    }

    public User findUserByUsername(String username){
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "username", username));
    }

    public Boolean findUserExistsById(Long id){
        return userRepo.existsById(id);
    }

    public List<User> findUserByPincode(String pincode){
        return userRepo.findByAddresses_Pincode(pincode);
    }

    public void saveUser(User user){
        userRepo.save(user);
    }

    public String deleteUser(Long id){
        User user = userRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
        userRepo.delete(user);
        return "UserId: "+user.getUserId()+" | User: "+user.getUsername()+" Deleted Successfully";
    }

    public String updateIdentity(SimpleUserDTO dto, UserPrincipal userPrincipal){
        User user = userPrincipal.user();

        if(!user.getUsername().equals(dto.getUsername())){
            userRepo.findByUsername(dto.getUsername()).ifPresent(
                    existingUser -> {
                        throw new ConflictResourceException(User.class, "username", dto.getUsername());
                    });
            user.setUsername(dto.getUsername());
        }
        user.setEmail(dto.getEmail());
        userRepo.save(user);
        return "UserId: "+user.getUserId()+" | User: "+user.getUsername()+" Updated Successfully";
    }

    public String updatePassword(ChangePasswordDTO dto, UserPrincipal userPrincipal){
        User user = userPrincipal.user();

        // Validate old password
        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        // Prevent same password reuse
        if (encoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from old password");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        user.setFirstLogin(false);
        userRepo.save(user);
        return "Password Updated Successfully";
    }

    public String addUserReview(UserPrincipal userPrincipal, ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProductId();
        User user = userPrincipal.user();
        Product product = productService.findProductById(productId);
        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProductReview(product);
        review.setUser(user);
        return reviewService.addReview(review);
    }

    public String updateUserReview(UserPrincipal userPrincipal, Long reviewId, ReviewDTO reviewDTO){
        User user = userPrincipal.user();
        Review review = reviewService.findReviewById(reviewId);
        review.setReview(reviewDTO.getReview());
        review.setStar(reviewDTO.getStar());
        return reviewService.updateReview(review);
    }

    public String deleteUserReview(UserPrincipal userPrincipal, Long reviewId){
        User user = userPrincipal.user();
        return reviewService.deleteReviewById(reviewId);
    }

    public List<Address> findUserAddress(UserPrincipal userPrincipal) {
        User user = userPrincipal.user();
        return user.getAddresses();
    }

    public String addAddress(UserPrincipal userPrincipal, Address address){
        User user = userPrincipal.user();
        if(user.getAddresses().isEmpty()){
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);
        userRepo.save(user);
        return "address Added Successfully";
    }

    public String updateAddress(UserPrincipal userPrincipal, Integer addIndex ,Address address){
        User user = userPrincipal.user();
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            throw new ResourceNotFoundException(Address.class, "addressIndex", addIndex);
        }
        addressList.set(addIndex, address);
        user.setAddresses(addressList);
        userRepo.save(user);
        return "address Updated Successfully";
    }

    public String removeAddress(UserPrincipal userPrincipal, Integer addIndex){
        User user = userPrincipal.user();
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            throw new ResourceNotFoundException(Address.class, "addressIndex", addIndex);
        }
        addressList.remove((int) addIndex);
        userRepo.save(user);
        return "address Removed Successfully";
    }

    public Set<Product> getUserWishlist(UserPrincipal userPrincipal){
        User user = userPrincipal.user();
        return user.getWishlist();
    }

    public Set<Product> addProductToWishlist(UserPrincipal userPrincipal, Long productId){
        User user = userPrincipal.user();
        Product product = productService.findProductById(productId);
        if(user.getWishlist() == null){
            user.setWishlist(new HashSet<>());
        }
        if(user.getWishlist().contains(product)){
            throw new ConflictResourceException(Product.class, "productId", productId);
        }
        user.getWishlist().add(product);
        userRepo.save(user);
        return user.getWishlist();
    }

    public Set<Product> removeProductFromWishlist(UserPrincipal userPrincipal, Long productId){
        User user = userPrincipal.user();
        Product product = productService.findProductById(productId);
        if (!user.getWishlist().contains(product)) {
            throw new ResourceNotFoundException(Product.class, "productId", productId);
        }
        user.getWishlist().remove(product);
        userRepo.save(user);
        return user.getWishlist();
    }

    public String emptyWishlist(UserPrincipal userPrincipal){
        User user = userPrincipal.user();
        user.getWishlist().clear();
        userRepo.save(user);
        return "Wishlist emptied successfully";
    }
}
