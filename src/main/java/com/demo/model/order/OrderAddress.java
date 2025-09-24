package com.demo.model.order;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddress {

    private String streetAddress;
    private String pincode;
    private String city;
    private String state;
}
