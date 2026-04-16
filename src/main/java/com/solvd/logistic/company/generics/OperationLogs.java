package com.solvd.logistic.company.generics;

import com.solvd.logistic.company.enums.ShipmentStatus;

public class OperationLogs<T> {
    private final T data;
    private final ShipmentStatus status;
    private final String description;
    public OperationLogs(T data,String description, ShipmentStatus status) {
        this.data = data;
        this.status = status;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Status: [" + status.getDescription() + "| Reason: "+ description +"] | Result: " + data.toString();
    }
}
