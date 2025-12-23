package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.exception.UnauthorizedAccessException;
import com.demo.model.product.Product;
import com.demo.model.order.*;
import com.demo.model.user.User;
import com.demo.model.user.UserPrincipal;
import com.demo.repo.OrderRepo;
import com.demo.repo.ProductRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//TODO Use AuthenticationPrincipal instead of userId in OrderService
@Log4j2
@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    InventoryService inventoryService;

    public Order createOrder(UserPrincipal userPrincipal, List<OrderItemDTO> itemDTOS, OrderAddress shippingAddress){
        User user = userPrincipal.user();
        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setUser(user);
        order.setOrderAddress(shippingAddress);
        List<OrderItem> orderItems = new ArrayList<>();
        if (itemDTOS == null || itemDTOS.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
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
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CREATED);
        inventoryService.updateStockAndReserveQuantity(order);
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
        String orderCode = "";
        String category = order.getItems().getFirst().getProduct().getCategory();
        String product = order.getItems().getFirst().getProduct().getProductName();

        category = (category.length() >= 3 ? category.substring(0,3) : category).toUpperCase();
        product = (product.length() >= 3 ? product.substring(0,3) : product).toUpperCase();
        long millis = System.currentTimeMillis();
        orderCode = category + product + millis;
        return orderCode;
    }

    public Order updateOrder(Order order){
        return orderRepo.save(order);
    }

    public Order findOrderByOrderCode(String orderCode){
        return orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderCode", orderCode));
    }

    public List<Order> findOrdersByPincode(String pincode) {
        return orderRepo.findByOrderAddress_Pincode(pincode);
    }

    public List<Order> findOrdersWithTotalMoreThan(BigDecimal total){
        return orderRepo.findByTotalGreaterThan(total);
    }

    public List<Order> findOrdersPlacedBetween(LocalDateTime time1, LocalDateTime time2){
        return orderRepo.findByOrderTimeBetween(time1, time2);
    }

    public List<Order> findOrdersByPincodeAndTotalMoreThan(String pincode, BigDecimal total){
        return orderRepo.findByOrderAddress_PincodeAndTotalGreaterThan(pincode, total);
    }

    public Order cancelOrder(UserPrincipal userPrincipal, String orderCode){
        Order order = orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderCode", orderCode));
        User inputUser = userPrincipal.user();
        User orderUser = order.getUser();
        if(!inputUser.equals(orderUser)){
            throw UnauthorizedAccessException.forAction("cancelOrder", Order.class);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        inventoryService.updateStockAndReserveQuantity(order);
        return orderRepo.save(order);
    }

    public Order returnOrder(UserPrincipal userPrincipal, String orderCode){
        Order order = orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, "orderCode", orderCode));
        User inputUser = userPrincipal.user();
        User orderUser = order.getUser();
        if(!inputUser.equals(orderUser)){
            throw UnauthorizedAccessException.forAction("returnOrder", Order.class);
        }
        if(order.getOrderStatus() != OrderStatus.DELIVERED){
            throw new IllegalStateException("Order has not been delivered");
        }
        order.setOrderStatus(OrderStatus.RETURNED);
        inventoryService.updateStockAndReserveQuantity(order);
        return orderRepo.save(order);
    }
}
