package com.demo.controller;

import com.demo.model.order.Order;
import com.demo.model.order.OrderConverter;
import com.demo.model.order.OrderResponseDTO;
import com.demo.model.shipment.Shipment;
import com.demo.model.shipment.ShipmentConverter;
import com.demo.model.shipment.ShipmentResponseDTO;
import com.demo.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ShipmentResponseDTO> createShipment(@PathVariable Long orderid ,@RequestParam String courierName){
        Shipment shipment = shipmentService.createShipment(orderid, courierName);
        ShipmentResponseDTO responseDTO = ShipmentConverter.toShipmentResponseDTO(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/order/deliver")
    public ResponseEntity<OrderResponseDTO> deliverOrder(@RequestParam String trackingId, @RequestParam String courierName){
        Order order = shipmentService.deliverOrder(trackingId, courierName);
        OrderResponseDTO responseDTO = OrderConverter.toOrderResponseDTO(order);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
