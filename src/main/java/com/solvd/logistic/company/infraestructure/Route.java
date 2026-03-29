package main.java.com.solvd.logistic.company.infraestructure;

public class Route {
    public Location startPoint;
    public Location endPoint;
    public double totalDistance;

    public Route(Location start, Location end, double distance) {
        this.startPoint = start;
        this.endPoint = end;
        this.totalDistance = distance;
    }
    @Override
    public String toString() { return "distance: " + totalDistance;}
}
