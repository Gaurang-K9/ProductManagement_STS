package com.demo.service;

import com.demo.exception.BadRequestException;
import com.demo.exception.ConflictResourceException;
import com.demo.exception.ForbiddenAccessException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import com.demo.model.payment.Payment;
import com.demo.model.payment.PaymentMethod;
import com.demo.model.payment.PaymentStatus;
import com.demo.model.shipment.Shipment;
import com.demo.model.user.Role;
import com.demo.model.user.User;
import com.demo.model.user.UserPrincipal;
import com.demo.repo.ShipmentRepo;
import com.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    ShipmentRepo shipmentRepo;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    UserRepo userRepo;

    private String generateTrackingId(String pincode){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String timePart = String.valueOf(System.currentTimeMillis());
        String lastFive = timePart.substring(timePart.length() - 5);

        return "TRK-" + pincode + "-" + date + "-" + lastFive;
    }

    public Shipment createShipment(Long orderId, Long deliveryAgentId){
        Order order = orderService.findOrderById(orderId);
        String pincode = order.getOrderAddress().getPincode();
        String trackingId = generateTrackingId(pincode);
        User deliveryAgent = userRepo.findById(deliveryAgentId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, "userId", deliveryAgentId));

        Shipment shipment = new Shipment();
        if(deliveryAgent.getRole() != Role.DELIVERY_AGENT){
            throw new BadRequestException("User with ID " + deliveryAgentId + " is not a delivery agent");
        }
        shipment.setDeliveryAgent(deliveryAgent);
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

    public Order deliverOrder(String trackingId, UserPrincipal userPrincipal){
        Shipment shipment = shipmentRepo.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(Shipment.class, "trackingId", trackingId));

        Long shipmentAgentId = shipment.getDeliveryAgent().getUserId();
        Long PrincipalAgentId = userPrincipal.user().getUserId();

        if (!shipmentAgentId.equals(PrincipalAgentId)) {
            throw ForbiddenAccessException.forAction("deliver", Shipment.class);
        }

        Order order = shipment.getOrder();
        if(order.getOrderStatus() == OrderStatus.CANCELLED){
            throw new ConflictResourceException("Cannot deliver cancelled order");
        }

        String orderCode = order.getOrderCode();
        Payment payment = paymentService.findByOrderCode(orderCode);

        if(payment.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            paymentService.updatePayment(payment);
        }
        else if(payment.getPaymentStatus() != PaymentStatus.SUCCESS){
            throw new ConflictResourceException("Cannot deliver unpaid order.");
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        inventoryService.updateStockAndReserveQuantity(order);
        return orderService.updateOrder(order);
    }

    public List<Shipment> findShipmentsByPincode(String pincode){
        return shipmentRepo.findByTrackingIdContaining(pincode);
    }

    public List<Shipment> findShipmentsBySpecificDate(LocalDate localDate){
        String date = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        return shipmentRepo.findByTrackingIdContaining(date);
    }

    public List<Shipment> findShipmentsByDeliveryAgent(Long deliveryAgentId){
        return shipmentRepo.findByDeliveryAgent_UserId(deliveryAgentId);
    }

    public List<Shipment> findShipmentsByOrderStatus(OrderStatus orderStatus){
        return shipmentRepo.findByOrder_OrderStatus(orderStatus);
    }

    public List<Shipment> findShipmentsByDeliveryAgentAndOrderStatus(Long deliveryAgentId, OrderStatus orderStatus){
        return shipmentRepo.findByDeliveryAgent_UserIdAndOrder_OrderStatus(deliveryAgentId, orderStatus);
    }
}
