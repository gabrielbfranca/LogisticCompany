package com.solvd.logistic.company.operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Billing extends Document {
    public double amount;
    public String currency;
    public static final Logger logger = LogManager.getLogger(Billing.class);
    public Billing(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public void generate() { logger.info("generate billing"); }
    @Override
    public String toString() { return "Total Bill: $" + amount;}
}
