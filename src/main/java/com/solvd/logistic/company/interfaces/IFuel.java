package com.solvd.logistic.company.interfaces;

import com.solvd.logistic.company.exceptions.InvalidFuelException;

public interface IFuel {
    public double getFuelLevel();
    public void refill(double amount) throws InvalidFuelException;
}
