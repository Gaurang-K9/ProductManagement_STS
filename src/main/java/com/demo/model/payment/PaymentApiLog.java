package com.demo.model.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String url;
    private String requestPayload;
    private String responseBody;
    private Integer responseStatus;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime responseAt;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
