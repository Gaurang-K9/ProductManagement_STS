package com.demo.service;

import com.demo.model.Address.Address;
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

    public Optional<User> findUserById(Long id){
        return userRepo.findById(id);
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
        if(userRepo.findById(id).isEmpty()){
            return "Could not locate resource";
        }
        userRepo.deleteById(id);
        return "User deleted successfully";
    }

    public String addUserReview(Long id, ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProductId();
        if(userRepo.findById(id).isEmpty() || productService.findProductById(productId).isEmpty()){
            return "Could Not Locate Resource";
        }

        Review review = ReviewConverter.toReview(reviewDTO);
        review.setProductReview(productService.findProductById(productId).get());
        review.setUser(userRepo.findById(id).get());
        return reviewService.addReview(review);
    }

    public String updateUserReview(Long userId, Long reviewId, ReviewDTO reviewDTO){
        Long productId = reviewDTO.getProductId();
        if(userRepo.findById(userId).isEmpty() || productService.findProductById(productId).isEmpty()){
            return "Could Not Locate Resource";
        }
        return reviewService.updateReview(reviewId ,reviewDTO);
    }

    public String updateUser(UserDTO updatedDto, Long userid){
        Optional<User> optional = userRepo.findById(userid);
        if(optional.isEmpty()){
            return  "Could Not Locate Resource";
        }
        User updateUser = optional.get();
        boolean isSameUsername = (updateUser.getUsername().contentEquals(updatedDto.getUsername()));//True for no username change and false for username change
        boolean sameExists = userRepo.existsByUsername(updatedDto.getUsername());//For changed username ideally should false
        //If true same name exists in database and should not update
        if(!isSameUsername && sameExists){
            return  "Cannot Update Username "+updatedDto.getUsername()+" already exists";
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
        Optional<User> optional = userRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        if(user.getAddresses().isEmpty()){
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);
        userRepo.save(user);
        return "Address Added Successfully";
    }

    public String updateAddress(Long id, Integer addIndex ,Address address){
        Optional<User> optional = userRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            return "Could Not Locate Resource: Address";
        }
        addressList.set(addIndex, address);
        user.setAddresses(addressList);
        userRepo.save(user);
        return "Address Updated Successfully";
    }

    public String removeAddress(Long id, Integer addIndex){
        Optional<User> optional = userRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        List<Address> addressList = user.getAddresses();
        if (addressList == null || addIndex < 0 || addIndex >= addressList.size()) {
            return "Could Not Locate Resource: Address";
        }
        addressList.remove((int) addIndex);
        userRepo.save(user);
        return "Address Removed Successfully";
    }
}
