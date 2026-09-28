package com.acme.feetoolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches previously computed fees so repeated lookups for the same principal are cheap.
 *
 * <p>Registered as a singleton by the auto-configuration and therefore shared across all
 * consumer threads.
 */
public class FeeCache {

    private final FeeCalculator calculator;
    private final Map<Money, Money> cache = new HashMap<>();
    private long hits = 0;

    public FeeCache(FeeCalculator calculator) {
        this.calculator = calculator;
    }

    public Money get(Money principal) {
        Money cached = cache.get(principal);
        if (cached != null) {
            hits++;
            return cached;
        }
        Money fee = calculator.calculateFee(principal);
        cache.put(principal, fee);
        return fee;
    }

    public long hits() {
        return hits;
    }
}
