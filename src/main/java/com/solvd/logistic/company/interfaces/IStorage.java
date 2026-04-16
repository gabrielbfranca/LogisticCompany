package com.solvd.logistic.company.interfaces;

import com.solvd.logistic.company.exceptions.InvalidSpaceException;

public interface IStorage {
    public double getAvailableCapacity();
    public boolean hasSpace(double weight);
    public void loadCapacity(double capacity) throws InvalidSpaceException;
}
