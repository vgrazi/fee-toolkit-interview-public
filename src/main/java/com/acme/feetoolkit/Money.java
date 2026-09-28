package com.acme.feetoolkit;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Immutable money value object.
 *
 * <p>Financial correctness rules baked in:
 * <ul>
 *   <li>amount is always a {@link BigDecimal} (never {@code double}/{@code float});</li>
 *   <li>arithmetic/comparison across differing currencies is rejected loudly.</li>
 * </ul>
 */
public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    /**
     * Currency-safe comparison. Comparing across currencies is a programming error, not a value
     * difference, so it throws rather than returning a meaningless ordering.
     */
    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount);
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Cannot operate on Money of different currencies: %s vs %s"
                            .formatted(this.currency, other.currency));
        }
    }
}
