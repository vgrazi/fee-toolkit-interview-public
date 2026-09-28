package com.acme.feetoolkit;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A single fee tier: applies {@code rate} to principals up to and including {@code upToAmount}.
 * Rates are fractions (0.020 == 2.0%).
 */
public record FeeTier(BigDecimal upToAmount, BigDecimal rate) {

    public FeeTier {
        Objects.requireNonNull(upToAmount, "upToAmount must not be null");
        Objects.requireNonNull(rate, "rate must not be null");
    }
}
