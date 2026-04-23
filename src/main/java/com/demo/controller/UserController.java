package com.demo.controller;

import com.demo.model.address.Address;
import com.demo.model.product.Product;
import com.demo.model.product.ProductConverter;
import com.demo.model.product.ProductResponseDTO;
import com.demo.model.user.*;
import com.demo.service.UserService;
import com.demo.service.auth.AuthService;
import com.demo.shared.ApiResponse;
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
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDTO>>> findAllUsers(
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse.fromPage(userService.findAllUsers(pageable)
                .map(UserConverter::toUserResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal){
        User user = userService.findUserById(userPrincipal.user().getUserId());
        UserProfileDTO responseDTO = UserConverter.toUserProfileDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findUserById(@PathVariable Long id){
        User user = userService.findUserById(id);
        UserResponseDTO responseDTO = UserConverter.toUserResponseDTO(user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @PutMapping("/update/profile")
    public ResponseEntity<ApiResponse<String>> updateIdentity(@Valid @RequestBody SimpleUserDTO updatedDTO, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.updateIdentity(updatedDTO, userPrincipal);
        
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@Valid @RequestBody ChangePasswordDTO dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.updatePassword(dto, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id){
        String response = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/address")
    public ResponseEntity<ApiResponse<List<Address>>> findUserAddress(@AuthenticationPrincipal UserPrincipal userPrincipal){
        List<Address> addresses = userService.findUserAddress(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(addresses));
    }

    @PostMapping("/address")
    public ResponseEntity<ApiResponse<String>> addAddress(@RequestBody Address address, @AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.addAddress(userPrincipal, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PutMapping("/address/{index}")
    public ResponseEntity<ApiResponse<String>> updateAddress(@RequestBody Address address, @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer index){
        String response = userService.updateAddress(userPrincipal, index, address);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @DeleteMapping("/address/{index}")
    public ResponseEntity<ApiResponse<String>> removeAddress(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer index){
        String response = userService.removeAddress(userPrincipal, index);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/wishlist")
    public ResponseEntity<ApiResponse<Set<ProductResponseDTO>>> getUserWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal){
        Set<Product> productSet = userService.getUserWishlist(userPrincipal);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(wishlist));
    }

    @PostMapping("/wishlist/add")
    public ResponseEntity<ApiResponse<Set<ProductResponseDTO>>> addProductToWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long productId){
        Set<Product> productSet = userService.addProductToWishlist(userPrincipal, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(wishlist));
    }

    @PatchMapping("/wishlist/remove")
    public ResponseEntity<ApiResponse<Set<ProductResponseDTO>>> removeProductFromWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long productId){
        Set<Product> productSet = userService.removeProductFromWishlist(userPrincipal, productId);
        Set<ProductResponseDTO> wishlist = ProductConverter.toProductResponseSet(productSet);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(wishlist));
    }

    @DeleteMapping("/wishlist/clear")
    public ResponseEntity<ApiResponse<String>> emptyWishlist(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = userService.emptyWishlist(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
