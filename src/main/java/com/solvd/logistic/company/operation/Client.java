package com.solvd.logistic.company.operation;

import com.solvd.logistic.company.annotations.Sensitive;

public record Client(@Sensitive String companyName, String contactEmail) {

    }

