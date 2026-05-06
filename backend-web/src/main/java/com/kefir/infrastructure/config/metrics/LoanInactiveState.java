package com.kefir.infrastructure.config.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class LoanInactiveState {

  private final AtomicInteger inactiveLoans = new AtomicInteger(0);

  public AtomicInteger getInactiveLoans() {
    return inactiveLoans;
  }

  public void set(int value) {
    inactiveLoans.set(value);
  }

  public void increment() {
    inactiveLoans.incrementAndGet();
  }

  public void decrement() {
    inactiveLoans.decrementAndGet();
  }
}
