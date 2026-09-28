package com.acme.feetoolkit;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Immutable configuration for fee calculation: the ordered tiers plus the rounding policy.
 *
 * <p>This type is intentionally Spring-agnostic so the core logic can be unit-tested and reused
 * without a container.
 */
public final class FeeSchedule {

    private final List<FeeTier> tiers;
    private final RoundingMode roundingMode;
    private final int scale;

    public FeeSchedule(List<FeeTier> tiers, RoundingMode roundingMode, int scale) {
        if (tiers == null || tiers.isEmpty()) {
            throw new IllegalArgumentException("tiers must not be empty");
        }
        this.roundingMode = Objects.requireNonNull(roundingMode, "roundingMode must not be null");
        this.scale = scale;
        this.tiers = List.copyOf(tiers); // defensive copy -> genuinely immutable
    }

    public List<FeeTier> tiers() {
        return tiers;
    }

    public RoundingMode roundingMode() {
        return roundingMode;
    }

    public int scale() {
        return scale;
    }
}
