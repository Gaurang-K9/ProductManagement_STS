package com.demo.service;

import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserResponseDTO;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public List<UserResponseDTO> findAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserResponseDTO> userlist = new ArrayList<>();
        users.forEach(single -> userlist.add(UserConverter.toUserResponseDTO(single)));
        return userlist;
    }

    public UserResponseDTO findUserById(Long id){
        Optional<User> user = userRepo.findById(id);
        return user.map(UserConverter::toUserResponseDTO).orElseGet(UserResponseDTO::new);
    }

    public String addUser(UserDTO userDTO){
        User saveUser = UserConverter.toUser(userDTO);
        userRepo.save(saveUser);
        return "User added successfully";
    }

    public String deleteUser(Long id){
        if(userRepo.findById(id).isEmpty()){
            return "Could not locate resource";
        }
        userRepo.deleteById(id);
        return "User deleted successfully";
    }
}
