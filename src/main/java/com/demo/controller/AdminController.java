package com.demo.controller;

import com.demo.model.user.Role;
import com.demo.model.user.SimpleUserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.service.CompanyService;
import com.demo.service.ProductService;
import com.demo.service.UserService;
import com.demo.service.auth.AuthService;
import com.demo.shared.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    ProductService productService;

    @PostMapping("/users/create/admin")
    public ResponseEntity<ApiResponse<String>> createAdmin(@Valid @RequestBody SimpleUserDTO userDTO){
        String response = authService.createBackendRole(userDTO, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PostMapping("/users/create/product-owner")
    public ResponseEntity<ApiResponse<String>> createProductOwner(@Valid @RequestBody SimpleUserDTO userDTO){
        String response = authService.createBackendRole(userDTO, Role.PRODUCT_OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PostMapping("/users/create/delivery-agent")
    public ResponseEntity<ApiResponse<String>> createDeliveryAgent(@Valid @RequestBody SimpleUserDTO userDTO){
        String response = authService.createBackendRole(userDTO, Role.DELIVERY_AGENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PatchMapping("/users/reset-password")
    public ResponseEntity<ApiResponse<UserLoginDTO>> resetPassword(@RequestParam String username){
        UserLoginDTO response = userService.adminResetPassword(username);
        String message = "User: "+username+" Password reset successfully";
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(message, response));
    }
}
