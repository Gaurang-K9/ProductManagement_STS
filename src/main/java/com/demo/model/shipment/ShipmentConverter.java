package com.demo.model.shipment;

public class ShipmentConverter {

    public static ShipmentResponseDTO toShipmentResponseDTO(Shipment shipment){
        ShipmentResponseDTO shipmentResponseDTO = new ShipmentResponseDTO();
        shipmentResponseDTO.setOrderCode(shipment.getOrder().getOrderCode());
        shipmentResponseDTO.setCourierName(shipment.getCourierName());
        shipmentResponseDTO.setTrackingId(shipment.getTrackingId());
        shipmentResponseDTO.setShippedAt(shipment.getShippedAt());
        return shipmentResponseDTO;
    }
}
