package com.demo.model.order;

import com.demo.model.user.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private String orderCode;
    private SimpleUserDTO user;
    private List<ItemResponseDTO> items;
    private LocalDateTime orderTime;
    private OrderAddress shippingAddress;
    private BigDecimal total;
    private OrderStatus orderStatus;
}
