package com.demo.repo;

import com.demo.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    Page<Order> findByOrderAddress_Pincode(String pincode, Pageable pageable);

    Page<Order> findByTotalGreaterThan(BigDecimal total, Pageable pageable);

    Page<Order> findByOrderAddress_PincodeAndTotalGreaterThan(String pincode, BigDecimal total, Pageable pageable);

    Page<Order> findByOrderTimeBetween(LocalDateTime time1, LocalDateTime time2, Pageable pageable);

    Optional<Order> findByOrderCode(String orderCode);
}
