package main.java.com.solvd.logistic.company.strategies;

import main.java.com.solvd.logistic.company.interfaces.IRouteTime;

public class RainyStrategy implements IRouteTime {

    @Override
    public float calculateRouteTime(float distance, float avgVelocity) {
        return (float) (distance / (avgVelocity * 0.8));
    }
}
