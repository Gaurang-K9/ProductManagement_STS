package com.demo.controller;

import com.demo.model.order.Order;
import com.demo.model.order.OrderConverter;
import com.demo.model.order.OrderResponseDTO;
import com.demo.model.order.OrderStatus;
import com.demo.model.shipment.Shipment;
import com.demo.model.shipment.ShipmentConverter;
import com.demo.model.shipment.ShipmentDeliveryAgentDTO;
import com.demo.model.shipment.ShipmentResponseDTO;
import com.demo.model.user.UserPrincipal;
import com.demo.service.ShipmentService;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    @Autowired
    ShipmentService shipmentService;

    @GetMapping("/find")
    public ResponseEntity<ApiResponse<ShipmentResponseDTO>> findByTrackingId(@RequestParam String trackingId){
        Shipment shipment = shipmentService.findShipmentByTrackingId(trackingId);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentResponseDTO>> findByShipmentId(@PathVariable Long id){
        Shipment shipment = shipmentService.findShipmentById(id);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @PostMapping("/create/{orderid}")
    public ResponseEntity<ApiResponse<ShipmentResponseDTO>> createShipment(@PathVariable Long orderid ,@RequestParam Long agentId){
        Shipment shipment = shipmentService.createShipment(orderid, agentId);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(responseDTO));
    }

    @PatchMapping("/order/deliver")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> deliverOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String trackingId){
        Order order = shipmentService.deliverOrder(trackingId, userPrincipal);
        OrderResponseDTO responseDTO = OrderConverter.toOrderResponseDTO(order);
        String message = "Delivered order successfully ";
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(responseDTO));
    }

    @GetMapping("/find/pincode")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentResponseDTO>>> findShipmentsByPincode(@RequestParam String pincode,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        var response = PageResponse
                .fromPage(shipmentService.findShipmentsByPincode(pincode, pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/find/date")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentResponseDTO>>> findShipmentsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        var response = PageResponse
                .fromPage(shipmentService.findShipmentsBySpecificDate(date, pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/find/status")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentResponseDTO>>> findShipmentsByDate(@RequestParam String status,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        var response = PageResponse
                .fromPage(shipmentService.findShipmentsByOrderStatus(OrderStatus.valueOf(status), pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/find/my-orders")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentDeliveryAgentDTO>>> findDeliveryAgentOrders(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.ASC) Pageable pageable){
        Long agentId = userPrincipal.user().getUserId();
        var response = PageResponse
                .fromPage(shipmentService.findShipmentsByDeliveryAgent(agentId, pageable)
                .map(ShipmentConverter::toShipmentDeliveryAgentDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @GetMapping("/find/my-orders/status")
    public ResponseEntity<ApiResponse<PageResponse<ShipmentDeliveryAgentDTO>>> findDeliveryAgentOrdersWithStatus(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String status,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.ASC) Pageable pageable){
        Long agentId = userPrincipal.user().getUserId();
        var response = PageResponse
                .fromPage(shipmentService.findShipmentsByDeliveryAgentAndOrderStatus(agentId, OrderStatus.valueOf(status), pageable)
                .map(ShipmentConverter::toShipmentDeliveryAgentDTO));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }
}
