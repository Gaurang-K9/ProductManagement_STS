package com.demo.controller;

import com.demo.model.order.*;
import com.demo.model.user.UserPrincipal;
import com.demo.service.OrderService;
import com.demo.shared.ApiResponse;
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
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> findOrderItemsById(@PathVariable Long id){
        Order order = orderService.findOrderById(id);
        OrderResponseDTO responseDTO =  OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CreateOrderRequest request) {
        Order serviceOrder = orderService.createOrder(userPrincipal, request.getItems(), request.getShippingAddress());
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        String message = "Order placed successfully for orderCode: "+order.getOrderCode();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(message, order));
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> changeShippingAddress(@RequestBody OrderAddress shippingAddress, @PathVariable Long id){
        Order serviceOrder = orderService.changeShippingAddress(id, shippingAddress);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(serviceOrder);
        String message = "Changed shipping address for orderCode: "+order.getOrderCode();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(message, order));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String ordercode){
        Order cancelOrder = orderService.cancelOrder(userPrincipal, ordercode);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(cancelOrder);
        String message = "Cancelled order for orderCode: "+order.getOrderCode();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(message, order));
    }

    @PatchMapping("/return")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> returnOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String ordercode){
        Order cancelOrder = orderService.returnOrder(userPrincipal, ordercode);
        OrderResponseDTO order = OrderConverter.toOrderResponseDTO(cancelOrder);
        String message = "Returned order for orderCode: "+order.getOrderCode();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(order));
    }

    @GetMapping("/pincode")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> findOrdersByPincode(@RequestParam String code,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse.
                fromPage(orderService.findOrdersByPincode(code, pageable)
                .map(OrderConverter::toOrderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/total/more")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> findOrdersWithTotalMoreThan(@RequestParam BigDecimal price,
           @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse
                .fromPage(orderService.findOrdersWithTotalMoreThan(price, pageable)
                .map(OrderConverter::toOrderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/date")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> findOrdersPlacedBetween(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time1, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam LocalDateTime time2,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse
                .fromPage(orderService.findOrdersPlacedBetween(time1, time2, pageable)
                .map(OrderConverter::toOrderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/code")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> findOrderByOrderCode(@RequestParam String id){
        Order order = orderService.findOrderByOrderCode(id);
        OrderResponseDTO response = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> findOrdersByPincodeAndTotalMoreThan(@RequestParam String pincode, @RequestParam BigDecimal total,
            @PageableDefault(sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable){
        var response = PageResponse
                .fromPage(orderService.findOrdersByPincodeAndTotalMoreThan(pincode, total, pageable)
                .map(OrderConverter::toOrderDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
