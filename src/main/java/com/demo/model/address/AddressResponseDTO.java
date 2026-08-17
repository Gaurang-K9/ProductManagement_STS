package com.demo.model.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressResponseDTO {

    private Integer id;
    private String streetAddress;
    private String city;
    private String state;
    private String pincode;
}
