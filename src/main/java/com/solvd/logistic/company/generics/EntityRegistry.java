package com.solvd.logistic.company.generics;


import com.solvd.logistic.company.resources.materials.Automobile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class EntityRegistry<T extends Automobile> {
    private Set<T> transports = new HashSet<>();
    public static final Logger logger = LogManager.getLogger(EntityRegistry.class);
    public void register(T item) {
        transports.add(item);

        logger.info("registered plate: {}", item.getLicensePlate());
    }
    public Set<T> getTransports() { return transports; }
}
