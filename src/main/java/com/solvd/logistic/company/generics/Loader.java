package main.java.com.solvd.logistic.company.generics;

import main.java.com.solvd.logistic.company.infraestructure.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Loader<T> {
    public static final Logger logger = LogManager.getLogger(Loader.class);
    public void load(T item, Warehouse warehouse) {
        logger.info("Loading {} into warehouse at {}", item.getClass().getSimpleName(), warehouse.location);
    }
}
