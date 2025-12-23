package com.demo.model.cart;

import com.demo.model.user.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private SimpleUserDTO user;
    private List<CartItemDTO> cartItems;
}
