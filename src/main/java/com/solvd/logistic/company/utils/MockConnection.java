package com.solvd.logistic.company.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class MockConnection {
    private static final Logger logger = LogManager.getLogger(MockConnection.class);
    private static final AtomicInteger IDS = new AtomicInteger();
    private final int id = IDS.incrementAndGet();
    public void open() {
        logger.info("Opened connection of {}", id);
    }
    public void close() {
        logger.info("closed connection of {}", id);
    }
    @Override
    public String toString() { return "Connection#" + id; }
}
