package com.demo.controller;

import com.demo.model.auth.AuthResponse;
import com.demo.model.auth.RefreshTokenRequest;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.model.user.UserPrincipal;
import com.demo.service.auth.AuthService;
import com.demo.shared.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @RequestMapping(value = {"/", "/?continue"})
    public void sendRedirect(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/swagger-ui.html");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> userLogin(@RequestBody UserLoginDTO loginDTO){
        AuthResponse response = authService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> userRegister(@Valid @RequestBody UserDTO userDTO){
        String response = authService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<String>> userLogout(@AuthenticationPrincipal UserPrincipal userPrincipal){
        String response = authService.logout(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
