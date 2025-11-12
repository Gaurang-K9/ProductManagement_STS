package com.demo.model.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponseDTO {

    private String orderCode;
    private String courierName;
    private String trackingId;
    private LocalDateTime shippedAt;
}
