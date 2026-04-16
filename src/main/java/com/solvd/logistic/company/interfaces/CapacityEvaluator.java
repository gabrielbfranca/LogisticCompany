package com.solvd.logistic.company.interfaces;

@FunctionalInterface
public interface CapacityEvaluator {
    boolean canLoad(double currentLoad, double incomingLoad, double maxCapacity);
}
