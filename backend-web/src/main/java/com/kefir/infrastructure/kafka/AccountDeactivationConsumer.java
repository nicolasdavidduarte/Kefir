package com.kefir.infrastructure.kafka;

import com.kefir.events.AccountDeactivationEvent;
import com.kefir.services.account.AccountService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountDeactivationConsumer {

  private static final Logger log = LoggerFactory.getLogger(AccountDeactivationConsumer.class);

  private final AccountService accountService;

  public AccountDeactivationConsumer(
      AccountService accountService) {
    this.accountService = accountService;
  }

  @KafkaListener(topics = "bank.account-security.events", groupId = "kefir-account-fraud-group")
  public void consume(AccountDeactivationEvent event) {
    Long accountId = event.accountId();

    accountService.suspend(accountId, event.reason());

    log.warn(
        "[FRAUD ALERT] Operation {} - Account {} suspended - {}",
        event.requestId(),
        accountId,
        Instant.now());
  }
}
