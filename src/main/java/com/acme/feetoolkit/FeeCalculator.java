package com.acme.feetoolkit;

import java.math.BigDecimal;

/**
 * Calculates the fee for a given principal using the configured {@link FeeSchedule}.
 * Stateless and thread-safe.
 */
public class FeeCalculator {

    private final FeeSchedule schedule;

    public FeeCalculator(FeeSchedule schedule) {
        this.schedule = schedule;
    }

    public Money calculateFee(Money principal) {
        BigDecimal rate = resolveRate(principal.amount());
        BigDecimal rounded = principal.amount()
                .multiply(rate)
                .setScale(schedule.scale(), schedule.roundingMode());
        return new Money(rounded, principal.currency());
    }

    /**
     * Picks the rate of the first tier whose {@code upToAmount} covers the principal.
     * Principals above the highest tier fall back to the top tier's rate.
     */
    private BigDecimal resolveRate(BigDecimal amount) {
        for (FeeTier tier : schedule.tiers()) {
            if (amount.compareTo(tier.upToAmount()) <= 0) {
                return tier.rate();
            }
        }
        return schedule.tiers().get(schedule.tiers().size() - 1).rate();
    }
}
