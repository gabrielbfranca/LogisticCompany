package main.java.com.solvd.logistic.company.operation;

import main.java.com.solvd.logistic.company.resources.human.Driver;
import main.java.com.solvd.logistic.company.infraestructure.Route;
import main.java.com.solvd.logistic.company.resources.materials.Truck;

public class Trip {
    private String tripId;
    public Truck vehicle;
    public Driver driver;
    public Route route;

    public Trip(String tripId, Truck vehicle, Driver driver, Route route) {
        this.tripId = tripId;
        this.vehicle = vehicle;
        this.driver = driver;
        this.route = route;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
