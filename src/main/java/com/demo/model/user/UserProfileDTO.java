package com.demo.model.user;

import com.demo.model.address.AddressResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private List<AddressResponseDTO> addresses = new ArrayList<>();
}