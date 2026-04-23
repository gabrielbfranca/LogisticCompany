package com.solvd.logistic.company;

import com.solvd.logistic.company.annotations.AuditAction;
import com.solvd.logistic.company.enums.ShipmentStatus;
import com.solvd.logistic.company.enums.VehicleType;
import com.solvd.logistic.company.interfaces.CapacityEvaluator;
import com.solvd.logistic.company.interfaces.FuelConsumptionEstimator;
import com.solvd.logistic.company.interfaces.RouteLabelFormatter;
import com.solvd.logistic.company.utils.*;
import com.solvd.logistic.company.enums.ResourceType;
import com.solvd.logistic.company.generics.EntityRegistry;
import com.solvd.logistic.company.generics.Loader;
import com.solvd.logistic.company.generics.OperationLogs;
import com.solvd.logistic.company.interfaces.IRouteTime;
import com.solvd.logistic.company.resources.human.Driver;
import com.solvd.logistic.company.exceptions.InvalidCoordinatesException;
import com.solvd.logistic.company.exceptions.InvalidFuelException;
import com.solvd.logistic.company.exceptions.InvalidSpaceException;
import com.solvd.logistic.company.infraestructure.Location;
import com.solvd.logistic.company.infraestructure.Route;
import com.solvd.logistic.company.infraestructure.Warehouse;

import com.solvd.logistic.company.operation.Billing;
import com.solvd.logistic.company.operation.Client;
import com.solvd.logistic.company.operation.Trip;
import com.solvd.logistic.company.resources.materials.Automobile;
import com.solvd.logistic.company.resources.materials.RefrigeratorTruck;
import com.solvd.logistic.company.resources.materials.Resource;
import com.solvd.logistic.company.strategies.RainyStrategy;
import com.solvd.logistic.company.strategies.TwoWayHighwayStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.toArray;

@AuditAction(value="Main execution", level="INFO")
public class LogisticCompany {
    static {
        System.setProperty("log4j.configurationFile", "classpath:log4j2.xml");
    }
    public static final Logger logger = LogManager.getLogger(LogisticCompany.class);
    public static void main(String[] args) throws InvalidCoordinatesException, InvalidFuelException, InvalidSpaceException {


        Location saoPaulo = new Location("São Paulo","SP","vila mariana");
        Location rioJaneiro = new Location("Rio de Janeiro","RJ","copacana");

        LinkedList<Location> path = Stream.of(saoPaulo, rioJaneiro)
                .collect(Collectors.toCollection(LinkedList::new));

        Map<String, IRouteTime> strategyMap = new HashMap<>();
        strategyMap.put("HIGHWAY", new TwoWayHighwayStrategy());
        strategyMap.put("RAINY", new RainyStrategy());

        List<Driver> driverSet = new ArrayList<>();
        Driver joao = new Driver("Gabriel", "gabrielbfranca@gmail.com");
        driverSet.add(joao);
        Inspector.FieldReport report = Inspector.inspect(joao);
        logger.info("Inspection report: {}", report);
        Set<Client> clients = new HashSet<>();
        Client pharmaCorp = new Client("Drogaria pacheco", "drogaria@gmail.com");
        clients.add(pharmaCorp);

        EntityRegistry<RefrigeratorTruck> fleet = new EntityRegistry<>();
        fleet.register(new RefrigeratorTruck("DHO-1234", 500, 20, 18, 800, VehicleType.TRUCK));
        fleet.register(new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800, VehicleType.TRUCK));

        RefrigeratorTruck truck = fleet.getTransports().stream().findFirst().orElse(null);
        assert truck != null;
        Inspector.audit(truck);

        Route rotaBR102 = new Route(saoPaulo, rioJaneiro, 200);
        Warehouse centroSP = new Warehouse("Centro São Paulo", 10);
        Inspector.audit(centroSP);
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
            logger.error(new OperationLogs<>(e.getMessage(), "GPS_FAILURE", ShipmentStatus.PENDING));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid coordinate
            truck.updateCoordinates(-23.5505, -46.6333);
            logger.info("fixed coordinates lat: {} and lon: {}", truck.lat, truck.lon);
        }

        try {
            truck.loadCapacity(1000);
        } catch(Exception e) {
            logger.error(new OperationLogs<>(e.getMessage(), "SPACE_ERROR",ShipmentStatus.PENDING));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid coordinate
            truck.loadCapacity(300);
            logger.info("fixed capacity {}", truck.capacity);
        }

        logger.info("Current Fuel: {}L", truck.getFuelLevel());


        try {
            truck.refill(10000);
        } catch(Exception e) {
            logger.error(new OperationLogs<>(e.getMessage(), "FUEL_ERROR", ShipmentStatus.PENDING));
            logger.error(e.getMessage());
        } finally { // reupdate unconditionally with a valid fuel
            truck.refill(100.0);
        }

        truck.setTargetTemperature(-4.0);
        logger.debug("Expected min temperature: {}°C", truck.getMinTemperature());

        truck.avgVelocity = 80.0f;
        truck.setTimeStrategy(strategyMap.get("HIGHWAY"));
        logger.info("On highway: {} hours", truck.calculateRouteTime(200.0f));
        logger.info(new OperationLogs<>(truck, "HIGHWAY_ETA", ShipmentStatus.IN_TRANSIT));
        truck.setTimeStrategy(strategyMap.get("RAINY"));
        logger.warn("Rainy condition detected! Adjusted time: {} hours", truck.calculateRouteTime(200.0f));
        logger.warn(new OperationLogs<>(truck, "WEATHER_ADJUSTED_ETA", ShipmentStatus.IN_TRANSIT));

        Trip activeTrip = new Trip("TRIP-001", truck, joao, rotaBR102);
        Billing tripBilling = new Billing(4500.50, "USD");

        logger.info("Cargo info: {}", medicalSupplies);
        logger.info("Trip ID: {}", activeTrip.tripId());
        logger.info("Driver: {}", activeTrip.driver().getName());
        logger.info("Vehicle Plate: {}", activeTrip.vehicle().getLicensePlate());
        logger.debug("Warehouse Location: {}", centroSP.location);
        logger.debug("Client Name: {}", pharmaCorp.companyName());
        logger.info("Route: From {} to {}", path.getFirst().cityName(), path.getLast().cityName());
        logger.info("Billing Amount: {}{}", tripBilling.amount, tripBilling.currency);
        logger.info("Required Temperature: {}°C", truck.getMinTemperature());
        logger.info("Vehicle Status: Plate {}, Fuel {}L, Temp {}°C",
                truck.getLicensePlate(),
                truck.getFuelLevel(),
                truck.getMinTemperature());

        // equals compare license plates since it's an ID.
        Automobile v2 = new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800, VehicleType.TRUCK);
        logger.debug("Comparing current truck with v2. Are vehicles same? {}", truck.equals(v2));

        Automobile v3 = new RefrigeratorTruck("ABC-123", 1000, 20, -20, 800, VehicleType.TRUCK);
        logger.debug("Comparing v3 with v2. Are vehicles same? {}", v3.equals(v2));

        // functional interface and stream tasks:
        RouteLabelFormatter routeLabelFormatter = (origin, destination) -> origin + " -> " + destination;
        FuelConsumptionEstimator fuelConsumptionEstimator = (distanceKm, litersPer100Km) ->
                (distanceKm / 100.0) * litersPer100Km;
        CapacityEvaluator capacityEvaluator = (currentLoad, incomingLoad, maxCapacity) ->
                currentLoad + incomingLoad <= maxCapacity;

        logger.info("Route Label: {}",
                routeLabelFormatter.format(path.getFirst().cityName(), path.getLast().cityName()));
        logger.info("Estimated fuel usage for route: {}L",
                fuelConsumptionEstimator.estimate(rotaBR102.totalDistance, 32));
        logger.info("Can load additional cargo? {}",
                capacityEvaluator.canLoad(truck.capacity, medicalSupplies.getWeight(), truck.getMaxPayload()));

        // running thread homework
        new Threading().start();
        new Thread(new ThreadingWithinterface()).start();
        ConnectionPool pool = ConnectionPool.getInstance();
        ExecutorService workers = Executors.newFixedThreadPool(7);
        Runnable task = () -> {
            try {
                String name = Thread.currentThread().getName();
                logger.info(name);
                MockConnection mc = pool.acquire();
                logger.info(name + "is with" + mc);
                Thread.sleep(2_000);
                pool.release(mc);
                logger.info(name + " released " + mc);
            } catch ( InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < 5; i++) workers.submit(task);

        workers.shutdown();
        try {
            workers.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.info("got this error {}", e.getMessage() );
        }


        CompletableFuture<?>[] futures = new CompletableFuture[7];

        for(int i = 0; i < 7; i++) {
                        // A. supplyAsync: Acquire the connection (This blocks if pool is empty)
                        futures[i] = CompletableFuture.supplyAsync(() -> {
                                    try {
                                        logger.info("Thread {} is requesting a connection in future", Thread.currentThread().getName());
                                        return pool.acquire();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, workers)
                                .thenAccept(mc -> {
                                    try {
                                        logger.info("Thread {} acquired {}", Thread.currentThread().getName(), mc);
                                        Thread.sleep(2000);
                                        pool.release(mc);
                                        logger.info("Thread {} released {}", Thread.currentThread().getName(), mc);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }).exceptionally(e -> { logger.info("this error was thrown {}", e.getMessage());
                                return null;
                                });

        }
        logger.info("Main thread waiting for tasks to complete...");
        CompletableFuture.allOf(futures).join();

        logger.info("All tasks completed. Shutting down.");
        workers.shutdown();
        pool.shutdown();

        // file execution
        Scanner scanner = new Scanner(System.in);
        logger.info("write the special words separated by spaces:");
        String input = scanner.nextLine();

        List<String> words = Arrays.asList(input.split(" "));

        try {
            CountWords.toFile("src/main/resources/results.txt",
                    CountWords.countSpecialCharacters("src/main/resources/article.txt", words)
            );
            logger.info("file successfully written");
        } catch(Exception e) {
            logger.error(e.getMessage());
        }


    }
}