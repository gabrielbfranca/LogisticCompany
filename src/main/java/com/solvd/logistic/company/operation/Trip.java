package com.solvd.logistic.company.operation;

import com.solvd.logistic.company.resources.human.Driver;
import com.solvd.logistic.company.infraestructure.Route;
import com.solvd.logistic.company.resources.materials.Truck;


public record Trip(String tripId, Truck vehicle, Driver driver, Route route) {

}
