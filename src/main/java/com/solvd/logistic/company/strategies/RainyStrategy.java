package com.solvd.logistic.company.strategies;

import com.solvd.logistic.company.interfaces.IRouteTime;

public class RainyStrategy implements IRouteTime {

    @Override
    public float calculateRouteTime(float distance, float avgVelocity) {
        return (float) (distance / (avgVelocity * 0.8));
    }
}
