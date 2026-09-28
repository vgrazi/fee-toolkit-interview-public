package com.acme.feetoolkit;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tests the auto-configuration with {@link ApplicationContextRunner} — full Spring wiring signal
 * without the cost/setup of a running {@code @SpringBootTest} app.
 */
class FeeToolkitAutoConfigurationTest {

    private static final Currency USD = Currency.getInstance("USD");

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(FeeToolkitAutoConfiguration.class))
            .withPropertyValues(
                    "acme.fees.rounding-mode=HALF_EVEN",
                    "acme.fees.scale=2",
                    "acme.fees.tiers[0].up-to-amount=1000",
                    "acme.fees.tiers[0].rate=0.020",
                    "acme.fees.tiers[1].up-to-amount=10000",
                    "acme.fees.tiers[1].rate=0.015");

    @Test
    void registersAllBeansAndBindsProperties() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(FeeSchedule.class);
            assertThat(context).hasSingleBean(FeeCalculator.class);
            assertThat(context).hasSingleBean(FeeCache.class);

            FeeCalculator calculator = context.getBean(FeeCalculator.class);
            Money fee = calculator.calculateFee(new Money(new BigDecimal("500.00"), USD));
            assertThat(fee.amount()).isEqualByComparingTo("10.00");
        });
    }

    @Test
    void consumerCanOverrideBeans() {
        runner.withUserConfiguration(CustomCalculatorConfig.class).run(context -> {
            // @ConditionalOnMissingBean must let the consumer's bean win.
            assertThat(context).hasSingleBean(FeeCalculator.class);
            assertThat(context.getBean(FeeCalculator.class))
                    .isInstanceOf(CustomCalculatorConfig.FixedFeeCalculator.class);
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomCalculatorConfig {

        @Bean
        FeeCalculator feeCalculator(FeeSchedule schedule) {
            return new FixedFeeCalculator(schedule);
        }

        static class FixedFeeCalculator extends FeeCalculator {
            FixedFeeCalculator(FeeSchedule schedule) {
                super(schedule);
            }

            @Override
            public Money calculateFee(Money principal) {
                return new Money(BigDecimal.ONE.setScale(2), principal.currency());
            }
        }
    }
}
