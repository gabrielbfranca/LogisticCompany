package com.solvd.logistic.company.enums;

public enum VehicleType {
    TRUCK("truck"),
    VAN("van"),
    MOTORCYCLE("motorcycle"),
    DRONE("drone");
    private final String name;
    VehicleType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
