package com.demo.controller;

import com.demo.model.address.Address;
import com.demo.model.product.Product;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.user.*;
import com.demo.service.UserService;
import com.demo.service.auth.AuthService;
import com.demo.shared.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    AuthService authService;

    @GetMapping("/all")
    public PageResponse<UserResponseDTO> findAllUsers(
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return PageResponse.fromPage(userService.findAllUsers(pageable)
                .map(UserConverter::toUserResponseDTO));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal){
        User user = userService.findUserById(userPrincipal.user().getUserId());
        UserProfileDTO responseDTO = UserConverter.toUserProfileDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        User user = userService.findUserById(id);
        UserResponseDTO responseDTO = UserConverter.toUserResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/add")    //For Testing
    public ResponseEntity<Map <String, String>> addUser(@RequestBody UserDTO userDTO){
        String response = authService.register(userDTO);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<Map <String, String>> updateIdentity(@Valid @RequestBody SimpleUserDTO updatedDTO, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.updateIdentity(updatedDTO, userPrincipal);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PutMapping("/update/password")
    public ResponseEntity<Map <String, String>> updatePassword(@Valid @RequestBody ChangePasswordDTO dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.updatePassword(dto, userPrincipal);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map <String, String>> deleteUser(@PathVariable Long id){
        String response = userService.deleteUser(id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> findUserAddress(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<Address> addresses = userService.findUserAddress(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

    @PostMapping("/address")
    public ResponseEntity<Map <String, String>> addAddress(@RequestBody Address address, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.addAddress(userPrincipal, address);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/address/{index}")
    public ResponseEntity<Map <String, String>> updateAddress(@RequestBody Address address, @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer index){
        String response = userService.updateAddress(userPrincipal, index, address);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("/address/{index}")
    public ResponseEntity<Map <String, String>> removeAddress(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer index){
        String response = userService.removeAddress(userPrincipal, index);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<Set <ProductResponseDTO>> getUserWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal){
        Set<Product> productSet = userService.getUserWishlist(userPrincipal);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(wishlist);
    }

    @PostMapping("/wishlist/add")
    public ResponseEntity<Set <ProductResponseDTO>> addProductToWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long productId){
        Set<Product> productSet = userService.addProductToWishlist(userPrincipal, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    @PatchMapping("/wishlist/remove")
    public ResponseEntity<Set <ProductResponseDTO>> removeProductFromWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long productId){
        Set<Product> productSet = userService.removeProductFromWishlist(userPrincipal, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(wishlist);
    }

    @DeleteMapping("/wishlist/clear")
    public ResponseEntity<Map <String, String>> emptyWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.emptyWishlist(userPrincipal);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
