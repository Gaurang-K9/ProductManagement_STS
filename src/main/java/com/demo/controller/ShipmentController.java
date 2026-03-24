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
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    ShipmentService shipmentService;

    @GetMapping("/find")
    public ResponseEntity<ShipmentResponseDTO> findByTrackingId(@RequestParam String trackingId){
        Shipment shipment = shipmentService.findShipmentByTrackingId(trackingId);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDTO> findByShipmentId(@PathVariable Long id){
        Shipment shipment = shipmentService.findShipmentById(id);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/create/{orderid}")
    public ResponseEntity<ShipmentResponseDTO> createShipment(@PathVariable Long orderid ,@RequestParam Long agentId){
        Shipment shipment = shipmentService.createShipment(orderid, agentId);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/order/deliver")
    public ResponseEntity<OrderResponseDTO> deliverOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String trackingId){
        Order order = shipmentService.deliverOrder(trackingId, userPrincipal);
        OrderResponseDTO responseDTO = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/find/pincode")
    public PageResponse<ShipmentResponseDTO> findShipmentsByPincode(@RequestParam String pincode,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        return PageResponse.fromPage(shipmentService.findShipmentsByPincode(pincode, pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
    }

    @GetMapping("/find/date")
    public PageResponse<ShipmentResponseDTO> findShipmentsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        return PageResponse.fromPage(shipmentService.findShipmentsBySpecificDate(date, pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
    }

    @GetMapping("/find/status")
    public PageResponse<ShipmentResponseDTO> findShipmentsByDate(@RequestParam String status,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.DESC) Pageable pageable){
        return PageResponse.fromPage(shipmentService.findShipmentsByOrderStatus(OrderStatus.valueOf(status), pageable)
                .map(ShipmentConverter::toShipmentResponseDTO));
    }

    @GetMapping("/find/my-orders")
    public PageResponse<ShipmentDeliveryAgentDTO> findDeliveryAgentOrders(@AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.ASC) Pageable pageable){
        Long agentId = userPrincipal.user().getUserId();
        return PageResponse.fromPage(shipmentService.findShipmentsByDeliveryAgent(agentId, pageable)
                .map(ShipmentConverter::toShipmentDeliveryAgentDTO));
    }

    @GetMapping("/find/my-orders/status")
    public PageResponse<ShipmentDeliveryAgentDTO> findDeliveryAgentOrdersWithStatus(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String status,
            @PageableDefault(sort = "shippedAt", direction = Sort.Direction.ASC) Pageable pageable){
        Long agentId = userPrincipal.user().getUserId();
        return PageResponse.fromPage(shipmentService.findShipmentsByDeliveryAgentAndOrderStatus(agentId, OrderStatus.valueOf(status), pageable)
                .map(ShipmentConverter::toShipmentDeliveryAgentDTO));
    }
}
