package com.demo.service;

import com.demo.exception.BadRequestException;
import com.demo.exception.ConflictResourceException;
import com.demo.exception.ResourceNotFoundException;
import com.demo.exception.ForbiddenAccessException;
import com.demo.model.product.Product;
import com.demo.model.order.*;
import com.demo.model.user.User;
import com.demo.model.user.UserPrincipal;
import com.demo.repo.OrderRepo;
import com.demo.repo.ProductRepo;
import com.demo.repo.UserRepo;
import com.demo.util.IdentifierGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class OrderService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    ShipmentService shipmentService;

    public Order createOrder(UserPrincipal userPrincipal, List<OrderItemDTO> itemDTOS, OrderAddress shippingAddress){
        User user = userPrincipal.user();
        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setUser(user);
        order.setOrderAddress(shippingAddress);
        List<OrderItem> orderItems = new ArrayList<>();
        if (itemDTOS == null || itemDTOS.isEmpty()) {
            throw new BadRequestException("Order items cannot be empty");
        }
        for(OrderItemDTO dto: itemDTOS){
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(Product.class, "productId", dto.getProductId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
            total = total.add(orderItem.getPrice());
            log.info("Total Inside For Loop: {}", total);
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        order.setTotal(total);
        log.info("Order Total: {}", total);
        String orderCode = generateOrderCode(order);
        order.setOrderCode(orderCode);
        order.setOrderStatus(OrderStatus.CREATED);
        inventoryService.placeOrder(order);
        return orderRepo.save(order);
    }

    public List<OrderItem> findOrderItemsById(Long orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderId", orderId));
        return order.getItems();
    }

    public Order findOrderById(Long orderId){
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderId", orderId));
    }

    public Order changeShippingAddress(Long orderId, OrderAddress shippingAddress){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderId", orderId));
        order.setOrderAddress(shippingAddress);
        return orderRepo.save(order);
    }

    private String generateOrderCode(Order order){
        String metadata = order.getOrderAddress().getPincode();
        return IdentifierGenerator.generate(IdentifierGenerator.ORDER_PREFIX, metadata);
    }

    public Order updateOrder(Order order){
        return orderRepo.save(order);
    }

    public Order findOrderByOrderCode(String orderCode){
        return orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderCode", orderCode));
    }

    public Page<Order> findOrdersByFilters(String pincode, BigDecimal minTotal, BigDecimal maxTotal, LocalDateTime from, LocalDateTime to, OrderStatus status, Long userId, String username, Pageable pageable) {
        if (userId != null && username != null) {
            throw new BadRequestException("Provide either userId or username, not both.");
        }

        return orderRepo.searchOrders(pincode, minTotal, maxTotal, from, to, status, userId, username, pageable);
    }

    public Page<Order> findOrdersByPincode(String pincode, Pageable pageable) {
        return orderRepo.findByOrderAddress_Pincode(pincode, pageable);
    }

    public Page<Order> findOrdersWithTotalMoreThan(BigDecimal total, Pageable pageable) {
        return orderRepo.findByTotalGreaterThan(total, pageable);
    }

    public Page<Order> findOrdersPlacedBetween(LocalDateTime time1, LocalDateTime time2, Pageable pageable){
        return orderRepo.findByOrderTimeBetween(time1, time2, pageable);
    }

    public Page<Order> findOrdersByPincodeAndTotalMoreThan(String pincode, BigDecimal total, Pageable pageable){
        return orderRepo.findByOrderAddress_PincodeAndTotalGreaterThan(pincode, total, pageable);
    }

    public Page<Order> findOrderByUserId(Long userId, Pageable pageable) {
        return orderRepo.findByUser_UserId(userId, pageable);
    }

    public Order cancelOrder(UserPrincipal userPrincipal, String orderCode){
        Order order = validateResourceOwnership(userPrincipal, orderCode, "cancel");
        order.setOrderStatus(OrderStatus.CANCELLED);
        inventoryService.cancelOrder(order);
        return orderRepo.save(order);
    }

    public Order returnOrder(UserPrincipal userPrincipal, String orderCode){
        Order order = validateResourceOwnership(userPrincipal, orderCode, "return");
        if(order.getOrderStatus() != OrderStatus.DELIVERED){
            throw new ConflictResourceException("Order has not been delivered");
        }
        order.setOrderStatus(OrderStatus.RETURNED);
        shipmentService.returnShipment(order.getShipment());
        inventoryService.returnOrder(order);
        return orderRepo.save(order);
    }

    private Order validateResourceOwnership(UserPrincipal userPrincipal, String orderCode, String action) {
        Order order = orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderCode", orderCode));
        if (!order.getUser().getUserId().equals(userPrincipal.user().getUserId())) {
            throw ForbiddenAccessException.forAction(action, Order.class);
        }
        return order;
    }
}
