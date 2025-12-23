package com.demo.controller;

import com.demo.model.order.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
//TODO Add AuthenticationPrincipal instead of userId in OrderController
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

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CreateOrderRequest request) {
        Order serviceOrder = orderService.createOrder(userPrincipal, request.getItems(), request.getShippingAddress());
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<OrderResponseDTO> changeShippingAddress(@RequestBody OrderAddress shippingAddress, @PathVariable Long id){
        Order serviceOrder = orderService.changeShippingAddress(id, shippingAddress);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String ordercode){
        Order cancelOrder = orderService.cancelOrder(userPrincipal, ordercode);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(cancelOrder);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PatchMapping("/return")
    public ResponseEntity<OrderResponseDTO> returnOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String ordercode){
        Order cancelOrder = orderService.returnOrder(userPrincipal, ordercode);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(cancelOrder);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/pincode")
    public ResponseEntity<List<OrderDTO>> findOrdersByPincode(@RequestParam String code){
        List<Order> pincodeOrders = orderService.findOrdersByPincode(code);
        List<OrderDTO> orders = OrderConverter.toOrderDTOList(pincodeOrders);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/total/more")
    public ResponseEntity<List<OrderDTO>> findOrdersWithTotalMoreThan(@RequestParam BigDecimal price){
        List<Order> totalOrders = orderService.findOrdersWithTotalMoreThan(price);
        List<OrderDTO> orders = OrderConverter.toOrderDTOList(totalOrders);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/date")
    public ResponseEntity<List<OrderDTO>> findOrdersPlacedBetween(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time1,
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time2){
        List<Order> dateOrders = orderService.findOrdersPlacedBetween(time1, time2);
        List<OrderDTO> orders = OrderConverter.toOrderDTOList(dateOrders);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/code")
    public ResponseEntity<OrderResponseDTO> findOrderByOrderCode(@RequestParam String id){
        Order order = orderService.findOrderByOrderCode(id);
        OrderResponseDTO response = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<OrderDTO>> findOrdersByPincodeAndTotalMoreThan(@RequestParam String pincode, @RequestParam BigDecimal total) {
        List<Order> orders = orderService.findOrdersByPincodeAndTotalMoreThan(pincode, total);
        List<OrderDTO> response = OrderConverter.toOrderDTOList(orders);
        return ResponseEntity.ok(response);
    }
}
