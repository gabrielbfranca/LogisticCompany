package main.java.com.solvd.logistic.company.resources.human;

import main.java.com.solvd.logistic.company.interfaces.IGPS;

public abstract class Worker implements IGPS {
    private String name;
    public double lat,lon;
    public Worker(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
