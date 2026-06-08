package com.company.shipment.repository;

import com.company.shipment.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Shipment findByTrackingId(String trackingId);
}
