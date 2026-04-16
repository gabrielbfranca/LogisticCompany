package com.solvd.logistic.company.resources.materials;

import com.solvd.logistic.company.enums.ResourceType;

public class Resource {
    private ResourceType type;
    private double weight;

    public Resource(ResourceType type, double weight) {
        this.type = type;
        this.weight = weight;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() { return "weight of the package:" + weight;}
}
