package main.java.com.solvd.logistic.company.resources.materials;

import main.java.com.solvd.logistic.company.interfaces.IFuel;
import main.java.com.solvd.logistic.company.interfaces.IGPS;

public abstract class Automobile implements IGPS, IFuel {
    private String licensePlate;
    public double fuel;
    public float avgVelocity;
    public double lat,lon;


    public Automobile(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Override
    public String toString() {
        return "Automobile Plate" + licensePlate;
    }

    public abstract float calculateRouteTime(float distance);


}
