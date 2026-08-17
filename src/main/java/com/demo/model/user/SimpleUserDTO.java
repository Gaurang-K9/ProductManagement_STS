package com.demo.model.user;

import com.demo.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserDTO {

    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = RegexConstants.NO_SPACES, message = "Username cannot contain spaces")
    private String username;

    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = RegexConstants.NAME_ONLY, message = "First name can only contain alphabets")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = RegexConstants.NAME_ONLY, message = "Last name can only contain alphabets")
    private String lastName;

    @NotBlank(message = "Mobile number cannot be empty")
    @Pattern(regexp = RegexConstants.MOBILE_NUMBER, message = "Mobile number can only contain numbers")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = RegexConstants.EMAIL, message = "Invalid email format")
    private String email;
}
