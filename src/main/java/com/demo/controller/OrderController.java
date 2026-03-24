package com.demo.controller;

import com.demo.model.order.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.OrderService;
import com.demo.shared.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public PageResponse<OrderDTO> findOrdersByPincode(@RequestParam String code,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        return PageResponse.fromPage(orderService.findOrdersByPincode(code, pageable)
                .map(OrderConverter::toOrderDTO));
    }

    @GetMapping("/total/more")
    public PageResponse<OrderDTO> findOrdersWithTotalMoreThan(@RequestParam BigDecimal price,
           @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        return PageResponse.fromPage(orderService.findOrdersWithTotalMoreThan(price, pageable)
                .map(OrderConverter::toOrderDTO));
    }

    @GetMapping("/date")
    public PageResponse<OrderDTO> findOrdersPlacedBetween(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time1, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time2,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        return PageResponse.fromPage(orderService.findOrdersPlacedBetween(time1, time2, pageable)
                .map(OrderConverter::toOrderDTO));
    }

    @GetMapping("/code")
    public ResponseEntity<OrderResponseDTO> findOrderByOrderCode(@RequestParam String id){
        Order order = orderService.findOrderByOrderCode(id);
        OrderResponseDTO response = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    public PageResponse<OrderDTO> findOrdersByPincodeAndTotalMoreThan(@RequestParam String pincode, @RequestParam BigDecimal total,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        return PageResponse.fromPage(orderService.findOrdersByPincodeAndTotalMoreThan(pincode, total, pageable)
                .map(OrderConverter::toOrderDTO));
    }
}
