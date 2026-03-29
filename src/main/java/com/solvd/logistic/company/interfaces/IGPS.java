package main.java.com.solvd.logistic.company.interfaces;

import main.java.com.solvd.logistic.company.exceptions.InvalidCoordinatesException;

import java.util.List;

public interface IGPS {
    public List<Double> getCurrentLocation();
    public void updateCoordinates(double lat, double lon) throws InvalidCoordinatesException;

}
