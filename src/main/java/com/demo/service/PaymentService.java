package com.demo.service;

import com.demo.exception.ResourceNotFoundException;
import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import com.demo.model.payment.*;
import com.demo.repo.PaymentApiLogRepo;
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
    PaymentApiLogRepo logRepository;

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
        String url = "https://jsonplaceholder.typicode.com/posts";
        try{
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> request = new HashMap<>();
            request.put("transactionId", payment.getTransactionId());
            request.put("amount", payment.getOrder().getTotal());
            request.put("method", payment.getPaymentMethod());

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            PaymentApiLog apiLog = new PaymentApiLog();
            apiLog.setTransactionId(payment.getTransactionId());
            apiLog.setUrl(url);
            apiLog.setRequestPayload(request.toString());
            apiLog.setResponseBody(response.getBody());
            apiLog.setResponseStatus(response.getStatusCode().value());
            apiLog.setPayment(payment);
            logRepository.save(apiLog);

            if(response.getStatusCode().is2xxSuccessful())
                return PaymentStatus.SUCCESS;
            else
                return PaymentStatus.FAILED;
        }
        catch(Exception e){
            PaymentApiLog apiLog = new PaymentApiLog();
            apiLog.setTransactionId(payment.getTransactionId());
            apiLog.setUrl(url);
            apiLog.setRequestPayload("Error while calling external payment API");
            apiLog.setResponseBody(e.getMessage());
            apiLog.setResponseStatus(500);
            apiLog.setPayment(payment);
            logRepository.save(apiLog);

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
