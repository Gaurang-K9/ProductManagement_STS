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
import java.util.Optional;

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
         boolean response = userService.findUserExistsById(id);
         if(!response){
             return new ResponseEntity<>(new CartDTO(), HttpStatus.NOT_FOUND);
         }
         Optional<Cart> optional = cartService.findCartByUserId(id);
         if(optional.isEmpty()){
             return new ResponseEntity<>(new CartDTO(), HttpStatus.OK);
         }
         CartDTO cart = CartConverter.toCartDTO(optional.get());
         return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/empty")
    public ResponseEntity<String> emptyCartById(@PathVariable Long id){
        String response = cartService.emptyCart(id);
        if(response.startsWith("Co")){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addItemsToCart(@RequestBody List<OrderItemDTO> items, @PathVariable Long id){
        String response = cartService.addItemsToCart(items, id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<String> addItemsToCartByUserId(@RequestBody List<OrderItemDTO> items, @PathVariable Long id){
        String response = cartService.addItemsToCartByUserId(items, id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/{userid}/cart/{cartid}/order")
    public ResponseEntity<OrderResponseDTO> placeCartToOrder(@PathVariable Long userid, @PathVariable Long cartid){
        if (!userService.findUserExistsById(userid)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = userService.findUserById(userid).get();
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
        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }
}
