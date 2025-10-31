package com.demo.model.cart;

import com.demo.model.Product.Product;
import com.demo.model.Product.ProductConverter;
import com.demo.model.order.OrderItemDTO;

import java.util.ArrayList;
import java.util.List;

public class CartItemConverter {

    public static CartItemDTO toCartItemDTO(CartItem cartItem){
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setItemId(cartItem.getItemId());
        Product product = cartItem.getProduct();
        cartItemDTO.setProduct(ProductConverter.toProductResponseDTO(product));
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }

    public static List<CartItemDTO> toCartItemDTOSList(List<CartItem> cartItems){
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for(CartItem item: cartItems){
            cartItemDTOS.add(toCartItemDTO(item));
        }
        return cartItemDTOS;
    }

    public static OrderItemDTO toOrderItemDTO(CartItem cartItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(cartItem.getProduct().getProductId());
        orderItemDTO.setQuantity(cartItem.getQuantity());
        return orderItemDTO;
    }

    public static List<OrderItemDTO> toOrderItemDTOSList(List<CartItem> cartItems){
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for(CartItem cartItem:  cartItems){
            orderItems.add(toOrderItemDTO(cartItem));
        }
        return orderItems;
    }
}
