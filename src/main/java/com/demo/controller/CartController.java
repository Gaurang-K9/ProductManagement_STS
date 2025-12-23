package com.demo.controller;

import com.demo.model.cart.*;
import com.demo.model.order.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//TODO Add AuthenticationPrincipal instead of userId in CartController
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/user")
    public ResponseEntity<CartDTO> returnUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal){
         Cart cart = cartService.returnUserCart(userPrincipal);
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

    @PostMapping("/user")
    public ResponseEntity<Map <String, String>> addItemsToUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody List<OrderItemDTO> items){
        String response = cartService.addItemsToUserCart(userPrincipal, items);
        Map <String, String> body = Map.of("response", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/{cartid}/order")
    public ResponseEntity<OrderResponseDTO> placeCartToOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long cartid){
        Order order = cartService.placeCartToOrder(userPrincipal, cartid);
        OrderResponseDTO orderResponseDTO = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }
}
