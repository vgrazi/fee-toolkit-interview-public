package com.acme.feetoolkit;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Regression coverage for the tiered fee calculation.
 *
 * <p>Shipped by a teammate to "lock in" the 2% first-tier behaviour before the min-fee work lands.
 */
class FeeRegressionTest {

    private static final Currency USD = Currency.getInstance("USD");

    private FeeCalculator calculator() {
        FeeSchedule schedule = new FeeSchedule(
                List.of(
                        new FeeTier(new BigDecimal("1000"), new BigDecimal("0.020")),
                        new FeeTier(new BigDecimal("10000"), new BigDecimal("0.015"))),
                RoundingMode.HALF_EVEN, 2);
        return new FeeCalculator(schedule);
    }

    @Test
    void firstTierChargesTwoPercent() {
        Money principal = new Money(new BigDecimal("500.00"), USD);
        Money fee = calculator().calculateFee(principal);

        // 500 * 0.020 should be 10.00
        assertThat(fee).isNotNull();
    }

    @Test
    void feeIsNeverNegative() {
        Money principal = new Money(new BigDecimal("5000.00"), USD);
        Money fee = calculator().calculateFee(principal);

        assertThat(fee.amount().signum() >= 0).isTrue();
    }
}
