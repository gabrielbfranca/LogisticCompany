package com.solvd.logistic.company.strategies;

import com.solvd.logistic.company.interfaces.IRouteTime;

public class TwoWayHighwayStrategy implements IRouteTime {

    @Override
    public float calculateRouteTime(float distance, float avgVelocity) {
        return (float) (distance / (avgVelocity * 1.2));
        // increase velocity to 10%
    }
}
