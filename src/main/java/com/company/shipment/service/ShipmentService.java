package com.company.shipment.service;

import com.company.shipment.model.Shipment;
import com.company.shipment.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public List<Shipment> findAll() {
        return repository.findAll();
    }

    public Shipment findByTrackingId(String trackingId) {
        return repository.findByTrackingId(trackingId);
    }

    public Shipment createShipment(Shipment shipment) {
        return repository.save(shipment);
    }

    public Shipment updateShipment(Long id, Shipment updated) {
        Shipment existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        existing.setTrackingId(updated.getTrackingId());
        existing.setSource(updated.getSource());
        existing.setDestination(updated.getDestination());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }

    public void deleteShipment(Long id) {
        repository.deleteById(id);
    }
}
