package com.solvd.logistic.company.resources.materials;

import com.solvd.logistic.company.enums.VehicleType;
import com.solvd.logistic.company.exceptions.InvalidCoordinatesException;
import com.solvd.logistic.company.exceptions.InvalidFuelException;
import com.solvd.logistic.company.exceptions.InvalidSpaceException;
import com.solvd.logistic.company.interfaces.IRouteTime;
import com.solvd.logistic.company.interfaces.IStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Truck extends Automobile implements IStorage {

    public double fuelCapacity;
    private double maxPayload;
    public float capacity;


    private IRouteTime timeStrategy;
    public static final Logger logger = LogManager.getLogger(Truck.class);
    public void setTimeStrategy(IRouteTime strategy) {
        this.timeStrategy = strategy;
    }

    public Truck(String licensePlate, double fuelCapacity, double maxPayload, VehicleType vehicleType) {
        super(licensePlate, vehicleType);
        this.fuelCapacity = fuelCapacity;
        this.maxPayload = maxPayload;
    }

    public double getMaxPayload() {
        return maxPayload;
    }

    public void setMaxPayload(double maxPayload) {
        this.maxPayload = maxPayload;
    }

    @Override
    public double getFuelLevel() {
        return this.fuel;
    }

    @Override
    public void refill(double amount) throws InvalidFuelException {
        if (amount < (fuelCapacity - fuel)) {
            this.fuel += amount;
            logger.info("Tank refilled. Current level: {}L", this.getFuelLevel());

        } else {
            throw new InvalidFuelException("too much, refill " + (fuelCapacity - fuel) + " instead");
        }

    }

    public void loadCapacity(double capacity) throws InvalidSpaceException {
        if (hasSpace(capacity)) {
            this.capacity = (float) capacity;
        }
        else {
            throw new InvalidSpaceException("Too much weight");
        }
    }

    @Override
    public List<Double> getCurrentLocation() {
        return List.of(lat,lon);
    }

    @Override
    public void updateCoordinates(double lat, double lon) throws InvalidCoordinatesException {
        if (lat < -90 || lat > 90) {
            throw new InvalidCoordinatesException("not a valid latitude");
        } else if (lon < -180 || lon > 180) {
            throw new InvalidCoordinatesException("not a valid longitude");
        } else {
            this.lat = lat;
            this.lon = lon;
        }
    }


    @Override
    public double getAvailableCapacity() {
        return maxPayload - capacity;
    }

    @Override
    public boolean hasSpace(double weight) {
        return weight <= (maxPayload - capacity);
    }


    @Override
    public float calculateRouteTime(float distance) {
        return timeStrategy.calculateRouteTime(distance, this.avgVelocity);
    }
}
