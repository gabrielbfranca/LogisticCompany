package main.java.com.solvd.logistic.company.resources.materials;

import main.java.com.solvd.logistic.company.interfaces.ITemperatureController;

import java.util.Objects;

public class RefrigeratorTruck extends Truck implements ITemperatureController {
    private double minTemperature;
    private double temperature;

    public RefrigeratorTruck(String licensePlate,
                             double fuelCapacity,
                             double temperature,
                             double minTemperature,
                             double maxPayload) {
        super(licensePlate, fuelCapacity, maxPayload);
        this.minTemperature = minTemperature;
        this.temperature = temperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RefrigeratorTruck)) return false;
        return getLicensePlate().equals(((RefrigeratorTruck) obj).getLicensePlate());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getLicensePlate());
    }

    @Override
    public double getCurrentTemperature() {
        return temperature;
    }

    @Override
    public void setTargetTemperature(double temp) {
        this.minTemperature = temp;
    }
}
