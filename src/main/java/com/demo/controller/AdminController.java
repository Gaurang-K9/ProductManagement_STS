package com.demo.controller;

import com.demo.model.user.Role;
import com.demo.model.user.SimpleUserDTO;
import com.demo.service.CompanyService;
import com.demo.service.ProductService;
import com.demo.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    CompanyService companyService;

    @Autowired
    ProductService productService;

    @PostMapping("/create/admin")
    public ResponseEntity<Map <String, String>> createAdmin(@RequestBody SimpleUserDTO userDTO){
        String response = userAuthService.createBackendRole(userDTO, Role.ADMIN);
        Map<String, String> body = new HashMap<>();
        body.put("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/create/product-owner")
    public ResponseEntity<Map <String, String>> createProductOwner(@RequestBody SimpleUserDTO userDTO){
        String response = userAuthService.createBackendRole(userDTO, Role.PRODUCT_OWNER);
        Map<String, String> body = new HashMap<>();
        body.put("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
