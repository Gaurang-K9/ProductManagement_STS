package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import com.demo.model.payment.Payment;
import com.demo.model.payment.PaymentMethod;
import com.demo.model.payment.PaymentRequest;
import com.demo.model.payment.PaymentStatus;
import com.demo.repo.PaymentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    PaymentRepo paymentRepo;

    @Autowired
    OrderService orderService;

    private String createTransactionId(Payment payment){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20251101
        String paymentType = payment.getPaymentMethod().name(); // e.g. CARD
        long millis = System.currentTimeMillis();

        return "TXN-" + date + "-" + paymentType + "-" + millis;
    }

    public Payment initiatePayment(Long orderId, PaymentRequest paymentRequest){
        Order order = orderService.findOrderById(orderId);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaidAt(LocalDateTime.now());
        String txnId = createTransactionId(payment);
        payment.setTransactionId(txnId);

        return paymentRepo.save(payment);
    }

    private PaymentStatus externalPaymentAPI(Payment payment){
        try{
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> request = new HashMap<>();
            request.put("transactionId", payment.getTransactionId());
            request.put("amount", payment.getOrder().getTotal());
            request.put("method", payment.getPaymentMethod());

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://jsonplaceholder.typicode.com/posts", request, String.class);

            if(response.getStatusCode().is2xxSuccessful())
                return PaymentStatus.SUCCESS;
            else
                return PaymentStatus.FAILED;
        }
        catch(Exception e){
            log.error("Error while calling external payment API for transactionId: {}", payment.getTransactionId(), e);
            return PaymentStatus.FAILED;
        }
    }

    public Payment processPayment(Long orderId){
        Payment payment = paymentRepo.findByOrder_Id(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Payment.class, "orderId", orderId));

        Order order = orderService.findOrderById(orderId);
        order.setOrderStatus(OrderStatus.PROCESSING);
        orderService.updateOrder(order);
        if(!payment.getPaymentMethod().equals(PaymentMethod.CASH_ON_DELIVERY)){
            PaymentStatus status = externalPaymentAPI(payment);
            payment.setPaymentStatus(status);
            payment.setPaidAt(LocalDateTime.now());
        }
        return paymentRepo.save(payment);
    }
}
