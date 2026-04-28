package com.demo.controller;

import com.demo.model.cart.*;
import com.demo.model.order.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.CartService;
import com.demo.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<CartDTO>> returnUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal){
         Cart cart = cartService.returnUserCart(userPrincipal);
         CartDTO cartDTO = CartConverter.toCartDTO(cart);
         return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(cartDTO));
    }

    @DeleteMapping("/{cartid}/empty")
    public ResponseEntity<ApiResponse<String>> emptyCartById(@PathVariable Long id){
        String response = cartService.emptyCart(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @PostMapping("/{cartid}/add-items")
    public ResponseEntity<ApiResponse<String>> addItemsToCart(@RequestBody List<OrderItemDTO> items, @PathVariable Long id){
        String response = cartService.addItemsToCart(items, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PostMapping("/user/add-items")
    public ResponseEntity<ApiResponse<String>> addItemsToUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody List<OrderItemDTO> items){
        String response = cartService.addItemsToUserCart(userPrincipal, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
    }

    @PostMapping("/{cartid}/order")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> placeCartToOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long cartid){
        Order order = cartService.placeCartToOrder(userPrincipal, cartid);
        OrderResponseDTO orderResponseDTO = OrderConverter.toOrderResponseDTO(order);
        String message = "Order placed successfully";
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(message, orderResponseDTO));
    }
}
