package com.solvd.logistic.company.utils;

public class Threading extends Thread {
    @Override
    public void run() {

        for (int i= 0; i < 10; i++) {

            System.out.printf("this is thread %s and this is the %d print%n", Thread.currentThread().getName(), i);
        }

    }
}
