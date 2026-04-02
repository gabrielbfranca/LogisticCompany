package main.java.com.solvd.logistic.company;

import main.java.com.solvd.logistic.company.enums.ResourceType;
import main.java.com.solvd.logistic.company.generics.EntityRegistry;
import main.java.com.solvd.logistic.company.generics.Loader;
import main.java.com.solvd.logistic.company.generics.OperationLogs;
import main.java.com.solvd.logistic.company.interfaces.IRouteTime;
import main.java.com.solvd.logistic.company.resources.human.Driver;
import main.java.com.solvd.logistic.company.exceptions.InvalidCoordinatesException;
import main.java.com.solvd.logistic.company.exceptions.InvalidFuelException;
import main.java.com.solvd.logistic.company.exceptions.InvalidSpaceException;
import main.java.com.solvd.logistic.company.infraestructure.Location;
import main.java.com.solvd.logistic.company.infraestructure.Route;
import main.java.com.solvd.logistic.company.infraestructure.Warehouse;

import main.java.com.solvd.logistic.company.operation.Billing;
import main.java.com.solvd.logistic.company.operation.Client;
import main.java.com.solvd.logistic.company.operation.Trip;
import main.java.com.solvd.logistic.company.resources.materials.Automobile;
import main.java.com.solvd.logistic.company.resources.materials.RefrigeratorTruck;
import main.java.com.solvd.logistic.company.resources.materials.Resource;
import main.java.com.solvd.logistic.company.strategies.RainyStrategy;
import main.java.com.solvd.logistic.company.strategies.TwoWayHighwayStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class LogisticCompany {
    static {
        System.setProperty("log4j.configurationFile", "main/resources/log4j2.xml");
    }
    public static final Logger logger = LogManager.getLogger(LogisticCompany.class);
    public static void main(String[] args) throws InvalidCoordinatesException, InvalidFuelException, InvalidSpaceException {


        Location saoPaulo = new Location("São Paulo","SP","vila mariana");
        Location rioJaneiro = new Location("Rio de Janeiro","RJ","copacana");

        LinkedList<Location> path = new LinkedList<>();
        path.add(saoPaulo);
        path.add(rioJaneiro);

        Map<String, IRouteTime> strategyMap = new HashMap<>();
        strategyMap.put("HIGHWAY", new TwoWayHighwayStrategy());
        strategyMap.put("RAINY", new RainyStrategy());

        List<Driver> driverSet = new ArrayList<>();
        Driver joao = new Driver("Gabriel", "gabrielbfranca@gmail.com");
        driverSet.add(joao);

        Set<Client> clients = new HashSet<>();
        Client pharmaCorp = new Client("Drogaria pacheco", "drogaria@gmail.com");
        clients.add(pharmaCorp);

        EntityRegistry<RefrigeratorTruck> fleet = new EntityRegistry<>();
        fleet.register(new RefrigeratorTruck("DHO-1234", 500, 20, 18, 800));
        fleet.register(new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800));

        RefrigeratorTruck truck = fleet.getTransports().stream().findFirst().orElse(null);

        Route rotaBR102 = new Route(saoPaulo, rioJaneiro, 200);
        Warehouse centroSP = new Warehouse("Centro São Paulo", 10);

        Queue<Resource> Cargo = new ArrayDeque<>();
        Resource medicalSupplies = new Resource(ResourceType.FRAGILE, 200);
        Cargo.add(medicalSupplies);

        Loader<Resource> resourceLoader = new Loader<>();
        Loader<RefrigeratorTruck> truckLoader = new Loader<>();

        resourceLoader.load(medicalSupplies, centroSP);
        truckLoader.load(truck, centroSP);

        logger.info("GPS: {}", truck.getCurrentLocation());
        try {
            logger.info("GPS: {} with {} license plate", truck.getCurrentLocation(), truck.getLicensePlate());
            truck.updateCoordinates(91,100);
        } catch(Exception e) {
            logger.error(new OperationLogs<>(e.getMessage(), "GPS_FAILURE"));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid coordinate
            truck.updateCoordinates(-23.5505, -46.6333);
            logger.info("fixed coordinates lat: {} and lon: {}", truck.lat, truck.lon);
        }

        try {
            truck.loadCapacity(1000);
        } catch(Exception e) {
            logger.error(new OperationLogs<>(e.getMessage(), "SPACE_ERROR"));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid coordinate
            truck.loadCapacity(300);
            logger.info("fixed capacity {}", truck.capacity);
        }

        logger.info("Current Fuel: {}L", truck.getFuelLevel());


        try {
            truck.refill(10000);
        } catch(Exception e) {
            logger.error(new OperationLogs<>(e.getMessage(), "FUEL_ERROR"));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid fuel
            truck.refill(100.0);
        }

        truck.setTargetTemperature(-4.0);
        logger.debug("Expected min temperature: {}°C", truck.getMinTemperature());

        truck.avgVelocity = 80.0f;
        truck.setTimeStrategy(strategyMap.get("HIGHWAY"));
        logger.info("On highway: {} hours", truck.calculateRouteTime(200.0f));
        logger.info(new OperationLogs<>(truck, "HIGHWAY_ETA"));
        truck.setTimeStrategy(strategyMap.get("RAINY"));
        logger.warn("Rainy condition detected! Adjusted time: {} hours", truck.calculateRouteTime(200.0f));
        logger.warn(new OperationLogs<>(truck, "WEATHER_ADJUSTED_ETA"));

        Trip activeTrip = new Trip("TRIP-001", truck, joao, rotaBR102);
        Billing tripBilling = new Billing(4500.50, "USD");

        logger.info("Cargo info: {}", medicalSupplies);
        logger.info("Trip ID: {}", activeTrip.getTripId());
        logger.info("Driver: {}", activeTrip.driver.getName());
        logger.info("Vehicle Plate: {}", activeTrip.vehicle.getLicensePlate());
        logger.debug("Warehouse Location: {}", centroSP.location);
        logger.debug("Client Name: {}", pharmaCorp.companyName);
        logger.info("Route: From {} to {}", path.getFirst().getCityName(), path.getLast().getCityName());
        logger.info("Billing Amount: {}{}", tripBilling.amount, tripBilling.currency);
        logger.info("Required Temperature: {}°C", truck.getMinTemperature());
        logger.info("Vehicle Status: Plate {}, Fuel {}L, Temp {}°C",
                truck.getLicensePlate(),
                truck.getFuelLevel(),
                truck.getMinTemperature());

        // equals compare license plates since it's an ID.
        Automobile v2 = new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800);
        logger.debug("Comparing current truck with v2. Are vehicles same? {}", truck.equals(v2));

        Automobile v3 = new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800);
        logger.debug("Comparing v3 with v2. Are vehicles same? {}", v3.equals(v2));

    }
}