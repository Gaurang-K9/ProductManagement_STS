package com.demo.repo;

import com.demo.model.payment.PaymentApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentApiLogRepo extends JpaRepository<PaymentApiLog, Long> {
}
