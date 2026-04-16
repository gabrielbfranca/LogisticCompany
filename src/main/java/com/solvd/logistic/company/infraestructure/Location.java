package com.solvd.logistic.company.infraestructure;

public class Location {
    private String cityName;
    private String state;
    public String streetAddress;

    public Location(String cityName, String state, String streetAddress) {
        this.cityName = cityName;
        this.state = state;
        this.streetAddress = streetAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
