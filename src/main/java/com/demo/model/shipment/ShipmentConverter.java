package com.demo.model.shipment;

import java.util.List;

public class ShipmentConverter {

    public static ShipmentResponseDTO toShipmentResponseDTO(Shipment shipment){
        ShipmentResponseDTO shipmentResponseDTO = new ShipmentResponseDTO();
        shipmentResponseDTO.setOrderCode(shipment.getOrder().getOrderCode());
        shipmentResponseDTO.setDeliveryAgent(shipment.getDeliveryAgent().getUsername());
        shipmentResponseDTO.setTrackingId(shipment.getTrackingId());
        shipmentResponseDTO.setShippedAt(shipment.getShippedAt());
        return shipmentResponseDTO;
    }

    public static ShipmentDeliveryAgentDTO toShipmentDeliveryAgentDTO(Shipment shipment){
        ShipmentDeliveryAgentDTO shipmentDeliveryAgentDTO = new ShipmentDeliveryAgentDTO();
        shipmentDeliveryAgentDTO.setTrackingId(shipment.getTrackingId());
        shipmentDeliveryAgentDTO.setDeliveryAgent(shipment.getDeliveryAgent().getUsername());
        shipmentDeliveryAgentDTO.setShippingAddress(shipment.getOrder().getOrderAddress());
        shipmentDeliveryAgentDTO.setTotal(shipment.getOrder().getTotal());
        return shipmentDeliveryAgentDTO;
    }

    public static List<ShipmentDeliveryAgentDTO> toShipmentDeliveryAgentDTOList(List<Shipment> shipments){
        return shipments.stream().map(ShipmentConverter::toShipmentDeliveryAgentDTO).toList();
    }
}
