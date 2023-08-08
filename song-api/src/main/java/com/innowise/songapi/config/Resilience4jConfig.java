package com.innowise.songapi.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(
                        TimeLimiterConfig
                                .from(TimeLimiterConfig.ofDefaults())
                                .timeoutDuration(Duration.ofSeconds(15))
                                .build()
                )
                .circuitBreakerConfig(
                        CircuitBreakerConfig
                                .from(CircuitBreakerConfig.ofDefaults())
                                .slidingWindowSize(5)
                                .minimumNumberOfCalls(5)
                                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                                .build()
                )
                .build());
    }
}
