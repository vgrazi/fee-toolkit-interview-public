package com.acme.feetoolkit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds consumer configuration under {@code acme.fees.*}.
 *
 * <pre>
 * acme:
 *   fees:
 *     rounding-mode: HALF_EVEN
 *     scale: 2
 *     tiers:
 *       - up-to-amount: 1000
 *         rate: 0.020
 *       - up-to-amount: 10000
 *         rate: 0.015
 * </pre>
 */
@ConfigurationProperties(prefix = "acme.fees")
public class FeeProperties {

    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private int scale = 2;
    private List<Tier> tiers = new ArrayList<>();

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    public void setTiers(List<Tier> tiers) {
        this.tiers = tiers;
    }

    public static class Tier {
        private BigDecimal upToAmount;
        private BigDecimal rate;

        public BigDecimal getUpToAmount() {
            return upToAmount;
        }

        public void setUpToAmount(BigDecimal upToAmount) {
            this.upToAmount = upToAmount;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }
    }
}
