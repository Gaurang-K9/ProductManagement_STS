package com.demo.repo;

import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderSearchRepository {

    Page<Order> searchOrders(String pincode, BigDecimal minTotal, BigDecimal maxTotal, LocalDateTime from, LocalDateTime to, OrderStatus status, Long userId, String username, Pageable pageable);
}
