package com.solvd.logistic.company.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static final int POOL_SIZE = 5;
    private final BlockingQueue<MockConnection> free = new LinkedBlockingQueue<>(POOL_SIZE);
    private ConnectionPool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            MockConnection mc = new MockConnection();
            mc.open();
            free.offer(mc);
        }
    }
    public MockConnection acquire() throws InterruptedException {
        return free.take();
    }
    public void release(MockConnection mc) throws InterruptedException {
        free.put(mc);
    }

    public void shutdown() {
        for (MockConnection mc : free) {
            mc.close();
            free.clear();
        }
    }

    public static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final ConnectionPool INSTANCE = new ConnectionPool();
    }


}
