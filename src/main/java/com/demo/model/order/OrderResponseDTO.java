package com.demo.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private String orderId;
    private String username;
    private String email;
    private List<ItemResponseDTO> items;
    private LocalDateTime orderTime;
    private OrderAddress shippingAddress;
    private BigDecimal total;
    private OrderStatus orderStatus;
}
