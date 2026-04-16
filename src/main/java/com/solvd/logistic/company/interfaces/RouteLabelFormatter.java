package com.solvd.logistic.company.interfaces;

@FunctionalInterface
public interface RouteLabelFormatter {
    String format(String origin, String destination);
}
