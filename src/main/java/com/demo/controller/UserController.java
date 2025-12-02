package com.demo.controller;

import com.demo.model.address.Address;
import com.demo.model.product.Product;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserResponseDTO;
import com.demo.service.UserService;
import com.demo.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserAuthService userAuthService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> userList = UserConverter.toUserResponseList(userService.findAllUsers());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        User user = userService.findUserById(id);
        UserResponseDTO responseDTO = UserConverter.toUserResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/add")    //For Testing
    public ResponseEntity<Map <String, String>> addUser(@RequestBody UserDTO userDTO){
        String response = userAuthService.register(userDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/update")
    public ResponseEntity<Map <String, String>> updateUser(@RequestBody UserDTO updatedDTO, @RequestParam Long userid){
        String response = userService.updateUser(updatedDTO, userid);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Map <String, String>> addReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){
        String response = userService.addUserReview(id, reviewDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{userid}/review/{reviewid}")
    public ResponseEntity<Map <String, String>> updateReview(@PathVariable Long userid, @PathVariable Long reviewid, @RequestBody ReviewDTO reviewDTO){
        String response = userService.updateUserReview(userid, reviewid, reviewDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/{userid}/review/{reviewid}")
    public ResponseEntity<Map <String, String>> updateReview(@PathVariable Long userid, @PathVariable Long reviewid) {
        String response = userService.deleteUserReview(userid, reviewid);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map <String, String>> deleteUser(@PathVariable Long id){
        String response = userService.deleteUser(id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<List<Address>> findUserAddress(@PathVariable Long id){
        User user = userService.findUserById(id);
        List<Address> addresses = user.getAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<Map <String, String>> addAddress(@RequestBody Address address, @PathVariable Long id){
        String response = userService.addAddress(id, address);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}/address/{index}")
    public ResponseEntity<Map <String, String>> updateAddress(@RequestBody Address address, @PathVariable Long id, @PathVariable Integer index){
        String response = userService.updateAddress(id, index, address);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/{id}/address/{index}")
    public ResponseEntity<Map <String, String>> removeAddress(@PathVariable Long id, @PathVariable Integer index){
        String response = userService.removeAddress(id, index);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/{id}/wishlist")
    public ResponseEntity<Set <ProductResponseDTO>> getUserWishlist(@PathVariable Long id){
        Set<Product> productSet = userService.getUserWishlist(id);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(wishlist);
    }

    @PostMapping("/{id}/wishlist")
    public ResponseEntity<Set <ProductResponseDTO>> addProductToWishlist(@PathVariable Long id, @RequestParam Long productId){
        Set<Product> productSet = userService.addProductToWishlist(id, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    @PatchMapping("/{id}/wishlist")
    public ResponseEntity<Set <ProductResponseDTO>> removeProductFromWishlist(@PathVariable Long id, @RequestParam Long productId){
        Set<Product> productSet = userService.removeProductFromWishlist(id, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(wishlist);
    }

    @DeleteMapping("/{id}/wishlist/clear")
    public ResponseEntity<Map <String, String>> emptyWishlist(@PathVariable Long id){
        String response = userService.emptyWishlist(id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
