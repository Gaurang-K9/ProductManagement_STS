package com.demo.service.auth;

import com.demo.exception.ConflictResourceException;
import com.demo.model.auth.AuthResponse;
import com.demo.model.user.*;
import com.demo.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
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
        user.setRole(Role.CUSTOMER);
        userRepo.save(user);
    }

    public String register(UserDTO userDTO){
        boolean response = userRepo.existsByUsername(userDTO.getUsername());
        if(response){
            throw new ConflictResourceException(User.class, "userName", userDTO.getUsername());
        }
        User saveUser = UserConverter.toUser(userDTO);
        saveUser(saveUser);
        return "User Registered successfully";
    }

    public AuthResponse login(UserLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        User user = userRepo.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateJWTToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.isFirstLogin(),  user.getRole());
    }

    public String randomPasswordGenerator(){
        char[] possibleCharacters = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?").toCharArray();
        int randomStrLength = 10;
        return RandomStringUtils.random( randomStrLength, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
    }

    public String createBackendRole(SimpleUserDTO userDTO, Role role){
        boolean response = userRepo.existsByUsername(userDTO.getUsername());
        if(response){
            throw new ConflictResourceException(User.class, "userName", userDTO.getUsername());
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        String tempPassword = randomPasswordGenerator();
        user.setPassword(encoder.encode(tempPassword));
        user.setRole(role);
        user.setFirstLogin(true);
        userRepo.save(user);
        log.info("New {} created | Username: {} | Temp Password: {}",role, user.getUsername(), tempPassword);
        return "Username: "+user.getUsername()+"| Password: "+tempPassword+" | Role: " + role;
    }
}
