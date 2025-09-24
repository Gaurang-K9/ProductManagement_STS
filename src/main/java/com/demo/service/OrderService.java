package com.demo.service;

import com.demo.model.Product.Product;
import com.demo.model.order.Order;
import com.demo.model.order.OrderAddress;
import com.demo.model.order.OrderItem;
import com.demo.model.order.OrderItemDTO;
import com.demo.model.user.User;
import com.demo.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        for(OrderItemDTO dto: itemDTOS){
            Product product = productService.findProductById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
            total = total.add(orderItem.getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        order.setTotal(total);
        return orderRepo.save(order);
    }

    public List<OrderItem> findOrderItemsById(Long orderId){
        Optional<Order> optional = orderRepo.findById(orderId);
        if(optional.isEmpty()){
            return new ArrayList<>();
        }
        return optional.get().getItems();
    }

    public Order changeShippingAddress(Long orderId, OrderAddress orderAddress){
        Optional<Order> optional = orderRepo.findById(orderId);
        if(optional.isEmpty()){
            return new Order();
        }
        Order order = optional.get();
        order.setOrderAddress(orderAddress);
        return orderRepo.save(order);
    }
}
