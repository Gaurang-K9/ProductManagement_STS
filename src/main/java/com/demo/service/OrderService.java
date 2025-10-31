package com.demo.service;

import com.demo.model.Address.Address;
import com.demo.model.Product.Product;
import com.demo.model.order.*;
import com.demo.model.user.User;
import com.demo.repo.OrderRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

//    @Autowired
//    AddressService addressService;

    public Order createOrder(Long userId, List<OrderItemDTO> itemDTOS, OrderAddress shippingAddress){
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setUser(user);
        order.setOrderAddress(shippingAddress);
        List<OrderItem> orderItems = new ArrayList<>();
        if (itemDTOS == null || itemDTOS.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }
        for(OrderItemDTO dto: itemDTOS){
            Product product = productService.findProductById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
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
        String orderId = generateOrderId(order);
        order.setOrderId(orderId);
        order.setOrderTime(LocalDateTime.now());
        return orderRepo.save(order);
    }

    public List<OrderItem> findOrderItemsById(Long orderId){
        Optional<Order> optional = orderRepo.findById(orderId);
        if(optional.isEmpty()){
            return new ArrayList<>();
        }
        return optional.get().getItems();
    }

    public Optional<Order> findOrderById(Long orderId){
        return orderRepo.findById(orderId);
    }

    public Order changeShippingAddress(Long orderId, OrderAddress shippingAddress){
        Optional<Order> optional = orderRepo.findById(orderId);
        if(optional.isEmpty()){
            return new Order();
        }
        Order order = optional.get();
        order.setOrderAddress(shippingAddress);
        return orderRepo.save(order);
    }

    private String generateOrderId(Order order){
        String orderId = "";
        String category = order.getItems().getFirst().getProduct().getCategory();
        String product = order.getItems().getFirst().getProduct().getProductName();

        category = (category.length() >= 3 ? category.substring(0,3) : category).toUpperCase();
        product = (product.length() >= 3 ? product.substring(0,3) : product).toUpperCase();
        long millis = System.currentTimeMillis();
        orderId = category + product + millis;
        return orderId;
    }
}
