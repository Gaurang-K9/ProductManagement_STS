package com.demo.model.address;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String streetAddress;
    @NotNull
    @Pattern(regexp="\\d{6}", message="Pincode must be exactly 6 digits")
    private String pincode;
    private String city;
    private String state;
}
