package com.solvd.logistic.company.interfaces;

@FunctionalInterface
public interface FuelConsumptionEstimator {
    double estimate(double distanceKm, double litersPer100Km);
}
