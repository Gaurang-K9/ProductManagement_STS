package com.demo.service.auth;

import com.demo.model.user.User;
import com.demo.model.user.UserConverter;
import com.demo.model.user.UserDTO;
import com.demo.model.user.UserLoginDTO;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public String register(UserDTO userDTO){
        boolean response = userRepo.existsByUsername(userDTO.getUsername());
        if(response){
            return "Cannot Register Username "+userDTO.getUsername()+" already exists";
        }
        User saveUser = UserConverter.toUser(userDTO);
        saveUser(saveUser);
        return "User Registered successfully";
    }

    public String login(UserLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        if(!authentication.isAuthenticated()){
            return "Could Not Login User. Try Again";
        }
        return jwtService.generateJWTToken(loginDTO.getUsername());
    }
}
