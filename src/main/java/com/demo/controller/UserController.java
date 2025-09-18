package com.demo.controller;

import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserResponseDTO;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<User> list = userService.findAllUsers();
        List<UserResponseDTO> userList = new ArrayList<>();
        list.forEach(single -> userList.add(UserConverter.toUserResponseDTO(single)));
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        Optional<User> optional = userService.findUserById(id);
        return optional.map(user -> new ResponseEntity<>(UserConverter.toUserResponseDTO(user), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new UserResponseDTO(), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        String response = userService.addUser(userDTO);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> addReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){
        String response = userService.addUserReview(id, reviewDTO);

        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
