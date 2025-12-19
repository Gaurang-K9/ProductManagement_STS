package com.demo.model.cart;

import com.demo.model.user.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private SimpleUserDTO user;
    private List<CartItemDTO> cartItems;
}
