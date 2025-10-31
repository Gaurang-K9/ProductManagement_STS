package com.demo.controller;

import com.demo.model.order.*;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderItemsById(@PathVariable Long id){
        Optional<Order> optional = orderService.findOrderById(id);
        if(optional.isEmpty()){
            return new ResponseEntity<>(new OrderResponseDTO(), HttpStatus.NOT_FOUND);
        }
        OrderResponseDTO order =  OrderConverter.toOrderResponseDTO(optional.get());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    @PostMapping("/test")
//    public ResponseEntity<CreateOrderRequest> testCreateOrder(@org.springframework.web.bind.annotation.RequestBody CreateOrderRequest request){
//        //System.out.println("testRequest: "+request);
//        System.out.println("ItemsDTO: "+request.getItems());
//        System.out.println("ShippingAddress: "+request.getShippingAddress());
//
//        return new ResponseEntity<>(request, HttpStatus.OK);
//    }
//
//    @PostMapping("/test-raw")
//    public ResponseEntity<String> testRawString(@RequestBody String raw){
//        return new ResponseEntity<>(raw, HttpStatus.OK);
//    }

    @PostMapping("/user/{id}")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequest request, @PathVariable Long id) {
        Order serviceOrder = orderService.createOrder(id, request.getItems(), request.getShippingAddress());
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<OrderResponseDTO> changeShippingAddress(@RequestBody OrderAddress shippingAddress, @PathVariable Long id){
        Order serviceOrder = orderService.changeShippingAddress(id, shippingAddress);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
