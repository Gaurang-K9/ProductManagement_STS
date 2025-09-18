package com.demo.service;

import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> findUserById(Long id){
        return userRepo.findById(id);
    }

    public String addUser(UserDTO userDTO){
        User user = userRepo.findByUsername(userDTO.getUsername());
        if(!user.getUsername().isEmpty()){
            return "Cannot Add Username "+user.getUsername()+" already exists";
        }
        User saveUser = UserConverter.toUser(userDTO);
        saveUser.setPassword(encoder.encode(userDTO.getPassword()));
        userRepo.save(saveUser);
        return "User added successfully";
    }

    public String deleteUser(Long id){
        if(userRepo.findById(id).isEmpty()){
            return "Could not locate resource";
        }
        userRepo.deleteById(id);
        return "User deleted successfully";
    }

    public String addUserReview(Long id, ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProduct_id();
        if(userRepo.findById(id).isEmpty() || productService.findProductById(productId).isEmpty()){
            return "Could not locate resource";
        }

        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProduct_review(productService.findProductById(productId).get());
        review.setUser(userRepo.findById(id).get());
        return reviewService.addReview(review);
    }
}
