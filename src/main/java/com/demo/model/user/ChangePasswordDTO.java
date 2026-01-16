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
public class ChangePasswordDTO {

    private String oldPassword;
    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = RegexConstants.NO_SPACES, message = "Password cannot contain spaces")
    private String newPassword;
}
