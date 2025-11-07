package com.demo.controller;

import com.demo.model.Address.Address;
import com.demo.model.cart.*;
import com.demo.model.order.*;
import com.demo.model.user.User;
import com.demo.service.CartService;
import com.demo.service.OrderService;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @GetMapping("/user/{id}")
    public ResponseEntity<CartDTO> findCartByUserId(@PathVariable Long id){
         Cart cart = cartService.findCartByUserId(id);
         CartDTO cartDTO = CartConverter.toCartDTO(cart);
         return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @DeleteMapping("/{id}/empty")
    public ResponseEntity<Map <String, String>> emptyCartById(@PathVariable Long id){
        String response = cartService.emptyCart(id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map <String, String>> addItemsToCart(@RequestBody List<OrderItemDTO> items, @PathVariable Long id){
        String response = cartService.addItemsToCart(items, id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Map <String, String>> addItemsToCartByUserId(@RequestBody List<OrderItemDTO> items, @PathVariable Long id){
        String response = cartService.addItemsToCartByUserId(items, id);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/user/{userid}/cart/{cartid}/order")
    public ResponseEntity<OrderResponseDTO> placeCartToOrder(@PathVariable Long userid, @PathVariable Long cartid){
        User user = userService.findUserById(userid);
        List<CartItem> cartItems = cartService.findCartItemsByCartId(cartid);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (user.getAddresses() == null || user.getAddresses().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<OrderItemDTO> orderItems = CartItemConverter.toOrderItemDTOSList(cartItems);

        Address userAddress = user.getAddresses().getFirst();
        OrderAddress shippingAddress = new OrderAddress();
        shippingAddress.setStreetAddress(userAddress.getStreetAddress());
        shippingAddress.setPincode(userAddress.getPincode());
        shippingAddress.setCity(userAddress.getCity());
        shippingAddress.setState(userAddress.getState());

        Order order = orderService.createOrder(userid, orderItems, shippingAddress);
        OrderResponseDTO orderResponseDTO = OrderConverter.toOrderResponseDTO(order);
        cartService.emptyCart(cartid);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }
}
