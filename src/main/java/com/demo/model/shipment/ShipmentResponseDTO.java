package com.demo.model.shipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponseDTO {

    private String orderCode;
    private String deliveryAgent;
    private String trackingId;
    private LocalDateTime shippedAt;
}
