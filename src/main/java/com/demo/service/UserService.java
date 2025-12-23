package com.demo.service;

import com.demo.exception.ConflictResourceException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.address.Address;
import com.demo.model.product.Product;
import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.*;
import com.demo.repo.ProductRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

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
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);

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
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);

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
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProductReview(product);
        review.setUser(user);
        return reviewService.addReview(review);
    }

    public String updateUserReview(UserPrincipal userPrincipal, Long reviewId, ReviewDTO reviewDTO){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        Review review = reviewService.findReviewById(reviewId);
        review.setReview(reviewDTO.getReview());
        review.setStar(reviewDTO.getStar());
        return reviewService.updateReview(review);
    }

    public String deleteUserReview(UserPrincipal userPrincipal, Long reviewId){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        return reviewService.deleteReviewById(reviewId);
    }

    public List<Address> findUserAddress(UserPrincipal userPrincipal) {
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        return user.getAddresses();
    }

    public String addAddress(UserPrincipal userPrincipal, Address address){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        user.getAddresses().add(address);
        userRepo.save(user);
        return "address Added Successfully";
    }

    public String updateAddress(UserPrincipal userPrincipal, Integer addIndex ,Address address){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
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
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            throw new ResourceNotFoundException(Address.class, "addressIndex", addIndex);
        }
        addressList.remove((int) addIndex);
        userRepo.save(user);
        return "address Removed Successfully";
    }

    public Set<Product> getUserWishlist(UserPrincipal userPrincipal){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        return user.getWishlist();
    }

    public Set<Product> addProductToWishlist(UserPrincipal userPrincipal, Long productId){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
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
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", productId));
        if (!user.getWishlist().contains(product)) {
            throw new ResourceNotFoundException(Product.class, "productId", productId);
        }
        user.getWishlist().remove(product);
        userRepo.save(user);
        return user.getWishlist();
    }

    public String emptyWishlist(UserPrincipal userPrincipal){
        Long userId = userPrincipal.user().getUserId();
        User user = findUserById(userId);
        user.getWishlist().clear();
        userRepo.save(user);
        return "Wishlist emptied successfully";
    }
}
