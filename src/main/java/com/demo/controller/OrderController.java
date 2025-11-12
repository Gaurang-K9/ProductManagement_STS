package com.demo.controller;

import com.demo.model.order.*;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderItemsById(@PathVariable Long id){
        Order order = orderService.findOrderById(id);
        OrderResponseDTO responseDTO =  OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequest request, @PathVariable Long id) {
        Order serviceOrder = orderService.createOrder(id, request.getItems(), request.getShippingAddress());
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<OrderResponseDTO> changeShippingAddress(@RequestBody OrderAddress shippingAddress, @PathVariable Long id){
        Order serviceOrder = orderService.changeShippingAddress(id, shippingAddress);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
}
