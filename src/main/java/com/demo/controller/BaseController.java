package com.demo.controller;

import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/", "/?continue"})
    public void sendRedirect(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/swagger-ui.html");
    }

    @PostMapping("/login")
    public ResponseEntity<String> userlogin(@RequestBody UserLoginDTO loginDTO){
        String response = userService.login(loginDTO);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody UserDTO userDTO){
        String response = userService.register(userDTO);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
