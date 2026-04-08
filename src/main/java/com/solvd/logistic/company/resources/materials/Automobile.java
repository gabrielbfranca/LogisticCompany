package main.java.com.solvd.logistic.company.resources.materials;

import com.solvd.logistic.company.enums.VehicleType;
import main.java.com.solvd.logistic.company.interfaces.IFuel;
import main.java.com.solvd.logistic.company.interfaces.IGPS;

public abstract class Automobile implements IGPS, IFuel {
    private String licensePlate;
    public double fuel;
    public float avgVelocity;
    public double lat,lon;
    private VehicleType vehicleType;

    public Automobile(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
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
