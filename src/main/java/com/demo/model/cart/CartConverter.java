package com.demo.model.cart;

import com.demo.model.user.SimpleUserDTO;
import com.demo.model.user.UserConverter;

import java.util.List;

public class CartConverter {

    public static CartDTO toCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        SimpleUserDTO user = UserConverter.toSimpleUserDTO(cart.getUser());
        cartDTO.setUser(user);
        List<CartItemDTO> cartItems = CartItemConverter.toCartItemDTOSList(cart.getCartItems());
        cartDTO.setCartItems(cartItems);
        return cartDTO;
    }
}
