package com.demo.service;

import com.demo.model.review.Review;
import com.demo.model.review.ReviewConverter;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.repo.UserRepo;
import com.demo.service.auth.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> findUserById(Long id){
        return userRepo.findById(id);
    }

    public String register(UserDTO userDTO){
        boolean response = userRepo.existsByUsername(userDTO.getUsername());
        if(response){
            return "Cannot Register Username "+userDTO.getUsername()+" already exists";
        }
        User saveUser = UserConverter.toUser(userDTO);
        saveUser(saveUser);
        return "User Registered successfully";
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

    public void saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public String updateUser(UserDTO updatedDto, Long userid){
        Optional<User> optional = userRepo.findById(userid);
        if(optional.isEmpty()){
            return  "Could Not Locate Resource";
        }
        User updateUser = optional.get();
        boolean isSameUsername = (updateUser.getUsername().contentEquals(updatedDto.getUsername()));//True for no username change and false for username change
        boolean sameExists = userRepo.existsByUsername(updatedDto.getUsername());   //For changed username ideally should false
                                                                                    //If true same name exists in database and should not update
        if(!isSameUsername && sameExists){
            return  "Cannot Update Username "+updatedDto.getUsername()+" already exists";
        }
        else if(!sameExists){
            updateUser.setUsername(updatedDto.getUsername());
            updateUser.setEmail(updatedDto.getEmail());
            updateUser.setPassword(updatedDto.getPassword());
            saveUser(updateUser);
            return "User Updated Successfully";
        }
        updateUser.setEmail(updatedDto.getEmail());
        updateUser.setPassword(updatedDto.getPassword());
        saveUser(updateUser);
        return "User Updated Successfully";
    }

    public String login(UserLoginDTO loginDTO) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        if(!authentication.isAuthenticated()){
            return "Could Not Login User. Try Again";
        }
        return jwtService.generateJWTToken(loginDTO.getUsername());
    }
}
