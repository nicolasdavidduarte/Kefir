package com.kefir.infrastructure.config.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class LoanMetrics {

  public LoanMetrics(
      MeterRegistry registry, LoanActiveState state, LoanInactiveState inactiveState) {

    Gauge.builder("loan_active_count", state.getActiveLoans(), AtomicInteger::get)
        .description("Active loans quantity")
        .register(registry);

    Gauge.builder("loan_inactive_count", inactiveState.getInactiveLoans(), AtomicInteger::get)
        .description("Inactive loans quantity")
        .register(registry);
  }
}
