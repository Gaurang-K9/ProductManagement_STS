package com.demo.controller;

import com.demo.model.review.ReviewDTO;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserResponseDTO;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> list = userService.findAllUsers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        UserResponseDTO user = userService.findUserById(id);
        if(user.getUsername().isEmpty()){
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        String response = userService.addUser(userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> addReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){
        String response = userService.addReview(id, reviewDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String response = userService.deleteUser(id);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
