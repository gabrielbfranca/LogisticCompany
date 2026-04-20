package com.solvd.logistic.company.infraestructure;

import com.solvd.logistic.company.annotations.AuditAction;
import com.solvd.logistic.company.exceptions.InvalidCoordinatesException;
import com.solvd.logistic.company.exceptions.InvalidSpaceException;
import com.solvd.logistic.company.interfaces.IGPS;
import com.solvd.logistic.company.interfaces.IStorage;

import java.util.List;

public class Warehouse implements IStorage, IGPS {
    public String location;
    public int loadingDocks;
    public double capacity;
    public double maxCapacity;
    public double lat,lon;
    public Warehouse(String location, int loadingDocks) {
        this.location = location;
        this.loadingDocks = loadingDocks;
    }


    @Override
    public double getAvailableCapacity() {
        return maxCapacity - capacity;
    }

    @Override
    public boolean hasSpace(double weight) {

        return weight <= (capacity - maxCapacity);
    }
    @AuditAction(value= "Warehouse load", level= "INFO")
    @Override
    public void loadCapacity(double capacity) throws InvalidSpaceException {
        if (hasSpace(capacity)) {
            this.capacity = capacity;
        }
        else {
            throw new InvalidSpaceException("Too much weight");
        }
    }


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
}
