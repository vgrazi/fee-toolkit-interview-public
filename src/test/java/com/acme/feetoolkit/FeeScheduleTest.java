package com.acme.feetoolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.Test;

class FeeScheduleTest {

    @Test
    void rejectsEmptyTiers() {
        assertThatThrownBy(() -> new FeeSchedule(List.of(), RoundingMode.HALF_EVEN, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tiers");
    }

    @Test
    void rejectsNullTiers() {
        assertThatThrownBy(() -> new FeeSchedule(null, RoundingMode.HALF_EVEN, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void copiesTiersDefensively() {
        var mutable = new java.util.ArrayList<>(List.of(
                new FeeTier(new BigDecimal("1000"), new BigDecimal("0.020"))));
        FeeSchedule schedule = new FeeSchedule(mutable, RoundingMode.HALF_EVEN, 2);
        mutable.clear();
        // Mutating the caller's list must not affect the schedule.
        org.assertj.core.api.Assertions.assertThat(schedule.tiers()).hasSize(1);
    }
}
