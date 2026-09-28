package com.acme.feetoolkit;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configures the fee toolkit for consumer applications.
 *
 * <p>Every bean is guarded with {@link ConditionalOnMissingBean}: a shared library must let
 * consumers override any bean, never force one on them.
 */
@AutoConfiguration
@EnableConfigurationProperties(FeeProperties.class)
public class FeeToolkitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FeeSchedule feeSchedule(FeeProperties props) {
        List<FeeTier> tiers = props.getTiers().stream()
                .map(t -> new FeeTier(t.getUpToAmount(), t.getRate()))
                .toList();
        return new FeeSchedule(tiers, props.getRoundingMode(), props.getScale());
    }

    @Bean
    @ConditionalOnMissingBean
    public FeeCalculator feeCalculator(FeeSchedule schedule) {
        return new FeeCalculator(schedule);
    }

    @Bean
    @ConditionalOnMissingBean
    public FeeCache feeCache(FeeCalculator calculator) {
        return new FeeCache(calculator);
    }
}
