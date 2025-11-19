package com.demo.controller;

import com.demo.model.payment.Payment;
import com.demo.model.payment.PaymentConverter;
import com.demo.model.payment.PaymentRequest;
import com.demo.model.payment.PaymentResponseDTO;
import com.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/initiate/order/{orderid}")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@PathVariable Long orderid, @RequestBody PaymentRequest paymentRequest){
        Payment payment = paymentService.initiatePayment(orderid, paymentRequest);
        PaymentResponseDTO response = PaymentConverter.toPaymentResponseDTO(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/process/order/{orderid}")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable Long orderid){
        Payment payment = paymentService.processPayment(orderid);
        PaymentResponseDTO response = PaymentConverter.toPaymentResponseDTO(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
