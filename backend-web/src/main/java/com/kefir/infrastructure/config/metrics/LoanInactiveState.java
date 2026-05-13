package com.kefir.infrastructure.config.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class LoanInactiveState {

  private final AtomicInteger inactiveLoans = new AtomicInteger(0);

  public AtomicInteger getInactiveLoans() {
    return inactiveLoans;
  }

  public void set(int value) {
    inactiveLoans.set(value);
  }
}
