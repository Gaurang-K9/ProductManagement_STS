package com.demo.controller;

import com.demo.model.Address.Address;
import com.demo.model.review.ReviewDTO;
import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserResponseDTO;
import com.demo.service.UserService;
import com.demo.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserAuthService userAuthService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> userList = UserConverter.toUserResponseList(userService.findAllUsers());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        Optional<User> optional = userService.findUserById(id);
        return optional.map(user -> new ResponseEntity<>(UserConverter.toUserResponseDTO(user), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new UserResponseDTO(), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")    //For Testing
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        String response = userAuthService.register(userDTO);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO updatedDTO, @RequestParam Long userid){
        String response = userService.updateUser(updatedDTO, userid);
        if(response.startsWith("Co")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (response.startsWith("Ca")) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<String> addReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){
        String response = userService.addUserReview(id, reviewDTO);

        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{userid}/review/{reviewid}")
    public ResponseEntity<String> updateReview(@PathVariable Long userid, @PathVariable Long reviewid, @RequestBody ReviewDTO reviewDTO){
        String response = userService.updateUserReview(userid, reviewid, reviewDTO);

        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String response = userService.deleteUser(id);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<List<Address>> findUserAddress(@PathVariable Long id){
        Optional<User> optional = userService.findUserById(id);
        if(optional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Address> addresses = optional.get().getAddresses();
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<String> addAddress(@RequestBody Address address, @PathVariable Long id){
        String response = userService.addAddress(id, address);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/address/{index}")
    public ResponseEntity<String> updateAddress(@RequestBody Address address, @PathVariable Long id, @PathVariable Integer index){
        String response = userService.updateAddress(id, index, address);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/address/{index}")
    public ResponseEntity<String> removeAddress(@PathVariable Long id, @PathVariable Integer index){
        String response = userService.removeAddress(id, index);
        if(response.startsWith("C")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
