package com.acme.feetoolkit;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

import org.junit.jupiter.api.Test;

class FeeCacheTest {

    private static final Currency USD = Currency.getInstance("USD");

    private FeeCache cache() {
        FeeSchedule schedule = new FeeSchedule(
                List.of(new FeeTier(new BigDecimal("1000"), new BigDecimal("0.020"))),
                RoundingMode.HALF_EVEN, 2);
        return new FeeCache(new FeeCalculator(schedule));
    }

    @Test
    void returnsSameResultForRepeatedLookups() {
        FeeCache cache = cache();
        Money principal = new Money(new BigDecimal("500.00"), USD);

        Money first = cache.get(principal);
        Money second = cache.get(principal);

        assertThat(second).isEqualTo(first);
    }

    @Test
    void countsCacheHits() {
        FeeCache cache = cache();
        Money principal = new Money(new BigDecimal("500.00"), USD);

        cache.get(principal); // miss -> compute + store
        cache.get(principal); // hit
        cache.get(principal); // hit

        assertThat(cache.hits()).isEqualTo(2);
    }
}
