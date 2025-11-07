package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.Address.Address;
import com.demo.model.Product.Product;
import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserDTO;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return "User deleted successfully";
    }

    public String addUserReview(Long id, ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProductId();
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
        Product product = productService.findProductById(productId);
        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProductReview(product);
        review.setUser(user);
        return reviewService.addReview(review);
    }

    public String updateUserReview(Long userId, Long reviewId, ReviewDTO reviewDTO){
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException(User.class, "userId", userId);
        }
        Review review = reviewService.findReviewById(reviewId);
        review.setReview(reviewDTO.getReview());
        review.setStar(reviewDTO.getStar());
        return reviewService.updateReview(review);
    }

    public String updateUser(UserDTO updatedDto, Long userId){
        User updateUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", userId));
        boolean isSameUsername = (updateUser.getUsername().contentEquals(updatedDto.getUsername()));//True for no username change and false for username change
        boolean sameExists = userRepo.existsByUsername(updatedDto.getUsername());//For changed username ideally should false
        //If true same name exists in database and should not update
        if(!isSameUsername && sameExists){
            return "Cannot Update Username "+updatedDto.getUsername()+" already exists";
        }
        else if(!sameExists){
            updateUser.setUsername(updatedDto.getUsername());
            updateUser.setEmail(updatedDto.getEmail());
            updateUser.setPassword(encoder.encode(updatedDto.getPassword()));
            userRepo.save(updateUser);
            return "User Updated Successfully";
        }
        updateUser.setEmail(updatedDto.getEmail());
        updateUser.setPassword(encoder.encode(updatedDto.getPassword()));
        userRepo.save(updateUser);
        return "User Updated Successfully";
    }

    public String addAddress(Long id, Address address){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
        if(user.getAddresses().isEmpty()){
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);
        userRepo.save(user);
        return "Address Added Successfully";
    }

    public String updateAddress(Long id, Integer addIndex ,Address address){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            throw new ResourceNotFoundException(Address.class, "addressIndex", addIndex);
        }
        addressList.set(addIndex, address);
        user.setAddresses(addressList);
        userRepo.save(user);
        return "Address Updated Successfully";
    }

    public String removeAddress(Long id, Integer addIndex){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", id));
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            throw new ResourceNotFoundException(Address.class, "addressIndex", addIndex);
        }
        addressList.remove((int) addIndex);
        userRepo.save(user);
        return "Address Removed Successfully";
    }
}
