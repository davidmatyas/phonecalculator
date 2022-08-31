package com.phonecompany.billing;

import java.util.HashMap;
import java.util.Map;

public enum Prices {
    HIGH_PRICE(1.0),
    LOW_PRICE(0.5),
    AFTER_LIMIT_PRICE(0.2);
    private static final Map<Double, Prices> BY_PRICE = new HashMap<>();

    Prices(double v) {

    }
}
