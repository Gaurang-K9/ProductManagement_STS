package com.demo.model.payment;

public class PaymentConverter {

    public static PaymentResponseDTO toPaymentResponseDTO(Payment payment){
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        paymentResponseDTO.setTransactionId(payment.getTransactionId());
        paymentResponseDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentResponseDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentResponseDTO.setPaidAt(payment.getPaidAt());
        return paymentResponseDTO;
    }
}
