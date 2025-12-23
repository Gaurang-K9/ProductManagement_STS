package com.demo.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String orderCode;
    private String username;
    private String email;
    private LocalDateTime orderTime;
    private OrderAddress shippingAddress;
    private BigDecimal total;
    private OrderStatus orderStatus;
}
