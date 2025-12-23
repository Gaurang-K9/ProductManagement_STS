package com.demo.repo;

import com.demo.model.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingId(String trackingId);

    List<Shipment> findByTrackingIdContaining(String trackingId);

    List<Shipment> findByDeliveryAgent_UserId(Long  deliveryAgentId);
}
