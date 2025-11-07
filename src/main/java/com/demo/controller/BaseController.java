package com.demo.controller;

import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.service.UserService;
import com.demo.service.auth.UserAuthService;
import jakarta.servlet.http.HttpServletResponse;
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
public class BaseController {

    @Autowired
    UserAuthService userService;

    @RequestMapping(value = {"/", "/?continue"})
    public void sendRedirect(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/swagger-ui.html");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody UserLoginDTO loginDTO){
        String response = userService.login(loginDTO);
        Map<String, String> body = new HashMap<>();
        if(response.startsWith("C")){
            body.put("response", response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
        body.put("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> userRegister(@RequestBody UserDTO userDTO){
        String response = userService.register(userDTO);
        Map<String, String> body = new HashMap<>();
        if(response.startsWith("C")){
            body.put("response", response);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }
        body.put("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}
