package com.demo.controller;

import com.demo.model.auth.AuthResponse;
import com.demo.model.auth.RefreshTokenRequest;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = {"/", "/?continue"})
    public void sendRedirect(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/swagger-ui.html");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody UserLoginDTO loginDTO){
        AuthResponse response = authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> userRegister(@Valid @RequestBody UserDTO userDTO){
        String response = authService.register(userDTO);
        Map<String, String> body = new HashMap<>();
        body.put("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
