package com.company.shipment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingId;
    private String source;
    private String destination;
    private String status;

    // Default constructor
    public Shipment() {
    }

    // Parameterized constructor
    public Shipment(Long id, String trackingId, String source, String destination, String status) {
        this.id = id;
        this.trackingId = trackingId;
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    // ================= GETTERS =================

    public Long getId() {
        return id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    // ================= SETTERS =================

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
