package com.demo.repo;

import com.demo.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_Id(Long orderId);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByOrder_OrderCode(String orderCode);
}
