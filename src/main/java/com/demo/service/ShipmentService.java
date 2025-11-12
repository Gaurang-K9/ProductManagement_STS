package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import com.demo.model.shipment.Shipment;
import com.demo.repo.ShipmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ShipmentService {

    @Autowired
    ShipmentRepo shipmentRepo;

    @Autowired
    OrderService orderService;

    private String generateTrackingId(String pincode){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String timePart = String.valueOf(System.currentTimeMillis());
        String lastFive = timePart.substring(timePart.length() - 5);

        return "TRK-" + pincode + "-" + date + "-" + lastFive;
    }

    public Shipment createShipment(Long orderId, String courierName){
        Order order = orderService.findOrderById(orderId);
        String pincode = order.getOrderAddress().getPincode();
        String trackingId = generateTrackingId(pincode);
        Shipment shipment = new Shipment();
        shipment.setCourierName(courierName);
        shipment.setOrder(order);
        shipment.setTrackingId(trackingId);
        shipment.setShippedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.SHIPPED);
        orderService.updateOrder(order);
        return shipmentRepo.save(shipment);
    }

    public Shipment updateShipment(Shipment shipment){
        return shipmentRepo.save(shipment);
    }

    public Shipment findShipmentById(Long shipmentId){
        return shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(Shipment.class, "shipmentId", shipmentId));
    }

    public Shipment findShipmentByTrackingId(String trackingId){
        return shipmentRepo.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(Shipment.class, "trackingId", trackingId));
    }

    public Order deliverOrder(String trackingId, String courierName){
        Shipment shipment = shipmentRepo
                .findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(Shipment.class, "trackingId", trackingId));

        String courier = shipment.getCourierName();
        if (!courierName.equalsIgnoreCase(courier)) {
            throw new IllegalArgumentException("Courier mismatch for the given tracking ID.");
        }

        Order order = shipment.getOrder();
        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)){
            throw new IllegalStateException("Cannot deliver cancelled order");
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        return orderService.updateOrder(order);
    }

}
