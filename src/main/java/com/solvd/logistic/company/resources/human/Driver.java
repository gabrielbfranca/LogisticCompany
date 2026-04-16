package com.solvd.logistic.company.resources.human;

import java.util.List;

public class Driver extends Worker {
    private String email;

    public Driver(String name, String email) {
        super(name);
        this.email = email;
    }

    @Override
    public String toString() {
        return "Driver:" + getName() + "E-mail:" + email;
    }

    @Override
    public List<Double> getCurrentLocation() {
        return List.of(lat,lon);
    }

    @Override
    public void updateCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
