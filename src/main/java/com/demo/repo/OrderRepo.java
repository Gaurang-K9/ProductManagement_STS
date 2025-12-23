package com.demo.repo;

import com.demo.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByOrderAddress_Pincode(String pincode);

    List<Order> findByTotalGreaterThan(BigDecimal total);

    List<Order> findByOrderAddress_PincodeAndTotalGreaterThan(String pincode, BigDecimal total);

    List<Order> findByOrderTimeBetween(LocalDateTime time1, LocalDateTime time2);

    Optional<Order> findByOrderCode(String orderCode);
}
