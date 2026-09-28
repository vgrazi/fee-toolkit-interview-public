package com.acme.feetoolkit;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

import org.junit.jupiter.api.Test;

class FeeCalculatorTest {

    private static final Currency USD = Currency.getInstance("USD");

    private FeeCalculator calculator(int scale, RoundingMode mode) {
        FeeSchedule schedule = new FeeSchedule(
                List.of(
                        new FeeTier(new BigDecimal("1000"), new BigDecimal("0.020")),
                        new FeeTier(new BigDecimal("10000"), new BigDecimal("0.015"))),
                mode, scale);
        return new FeeCalculator(schedule);
    }

    @Test
    void appliesFirstTierRate() {
        Money fee = calculator(2, RoundingMode.HALF_EVEN)
                .calculateFee(new Money(new BigDecimal("500.00"), USD));
        // 500 * 0.020 = 10.00
        assertThat(fee.amount()).isEqualByComparingTo("10.00");
        assertThat(fee.currency()).isEqualTo(USD);
    }

    @Test
    void appliesSecondTierRate() {
        Money fee = calculator(2, RoundingMode.HALF_EVEN)
                .calculateFee(new Money(new BigDecimal("5000.00"), USD));
        // 5000 * 0.015 = 75.00
        assertThat(fee.amount()).isEqualByComparingTo("75.00");
    }

    @Test
    void principalAboveTopTierUsesTopTierRate() {
        Money fee = calculator(2, RoundingMode.HALF_EVEN)
                .calculateFee(new Money(new BigDecimal("50000.00"), USD));
        // 50000 * 0.015 = 750.00
        assertThat(fee.amount()).isEqualByComparingTo("750.00");
    }

    @Test
    void usesBankersRoundingHalfEven() {
        // 106.25 * 0.020 = 2.1250 -> HALF_EVEN rounds to the even neighbour -> 2.12
        Money fee = calculator(2, RoundingMode.HALF_EVEN)
                .calculateFee(new Money(new BigDecimal("106.25"), USD));
        assertThat(fee.amount()).isEqualByComparingTo("2.12");
    }

    @Test
    void preservesPrincipalCurrency() {
        Currency eur = Currency.getInstance("EUR");
        Money fee = calculator(2, RoundingMode.HALF_EVEN)
                .calculateFee(new Money(new BigDecimal("500.00"), eur));
        assertThat(fee.currency()).isEqualTo(eur);
    }
}
