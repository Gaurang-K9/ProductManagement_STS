package com.demo.repo;

import com.demo.model.order.OrderStatus;
import com.demo.model.shipment.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingId(String trackingId);

    Page<Shipment> findByTrackingIdContaining(String trackingId, Pageable pageable);

    Page<Shipment> findByDeliveryAgent_UserId(Long  deliveryAgentId, Pageable pageable);

    Page<Shipment> findByOrder_OrderStatus(OrderStatus orderStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"order", "deliveryAgent"})
    Page<Shipment> findByDeliveryAgent_UserIdAndOrder_OrderStatus(Long deliveryAgentId, OrderStatus orderStatus, Pageable pageable);
}
