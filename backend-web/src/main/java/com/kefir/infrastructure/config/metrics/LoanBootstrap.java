package com.kefir.infrastructure.config.metrics;

import com.kefir.repositories.LoanRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoanBootstrap {

  private final LoanRepository repository;
  private final LoanActiveState state;
  private final LoanInactiveState inactiveState;

  public LoanBootstrap(
      LoanRepository repository, LoanActiveState state, LoanInactiveState inactiveState) {
    this.repository = repository;
    this.state = state;
    this.inactiveState = inactiveState;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    final int active = repository.countByStatus(1);
    final int inactive = repository.countByStatus(2);
    state.set(active);
    inactiveState.set(inactive);
  }
}
