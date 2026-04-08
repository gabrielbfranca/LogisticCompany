package com.solvd.logistic.company.enums;

public enum ShipmentStatus {
    PENDING("awaiting shipment..."),
    IN_TRANSIT("On the way"),
    DELIVERED("Arrived");

    private final String description;
    ShipmentStatus(String description) {
        this.description = description;
    }
    public String getDescription() { return description; }
}
