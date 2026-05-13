package com.kefir.infrastructure.config.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class LoanActiveState {

  private final AtomicInteger activeLoans = new AtomicInteger(0);

  public AtomicInteger getActiveLoans() {
    return activeLoans;
  }

  public void set(int value) {
    activeLoans.set(value);
  }

  public void increment() {
    activeLoans.incrementAndGet();
  }
}
