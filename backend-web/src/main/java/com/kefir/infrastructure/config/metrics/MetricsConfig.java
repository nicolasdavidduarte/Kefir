package com.kefir.infrastructure.config.metrics;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.config.MeterFilter;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
  @Bean
  public TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }

  @Bean
  public CountedAspect countedAspect(MeterRegistry registry) {
    return new CountedAspect(registry);
  }

  // --- MeterFilters

  @Bean
  public MeterFilter commonTags() {
    return MeterFilter.commonTags(
        Tags.of(
            "app", "kefir",
            "env", "dev"));
  }

  @Bean
  public MeterFilter httpLimiter() {
    return MeterFilter.maximumAllowableTags("http.server.requests", "uri", 50, MeterFilter.deny());
  }

  @Bean
  public MeterFilter denyExecutorMetrics() {
    return MeterFilter.denyNameStartsWith("executor");
  }

  @Bean
  public Timer loanGetTimer(MeterRegistry meterRegistry) {
    return Timer.builder("loan.get")
        .description("Time taken to get a loan by id")
        .serviceLevelObjectives(
            Duration.ofMillis(5),
            Duration.ofMillis(10),
            Duration.ofMillis(25),
            Duration.ofMillis(50),
            Duration.ofMillis(100))
        .tag("operation", "getById")
        .register(meterRegistry);
  }
}
