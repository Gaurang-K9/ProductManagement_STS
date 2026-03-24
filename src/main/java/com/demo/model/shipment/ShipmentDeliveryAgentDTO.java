package com.demo.model.shipment;

import com.demo.model.order.OrderAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDeliveryAgentDTO {

    private String trackingId;
    private String deliveryAgent;
    private OrderAddress shippingAddress;
    private BigDecimal total;
}
