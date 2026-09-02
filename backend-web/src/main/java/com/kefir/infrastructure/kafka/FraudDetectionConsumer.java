package com.kefir.infrastructure.kafka;

import com.kefir.events.AccountDeactivationEvent;
import com.kefir.events.LoanRequestAuditEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FraudDetectionConsumer {

  private static final Logger log = LoggerFactory.getLogger(FraudDetectionConsumer.class);

  private final KafkaTemplate<Long, AccountDeactivationEvent> kafkaTemplate;

  private final Map<Long, List<Instant>> loanActivityMap = new ConcurrentHashMap<>();

  private static final int MAX_TRANSACTIONS = 2;
  private static final Duration TIME_WINDOW = Duration.ofMinutes(1);

  public FraudDetectionConsumer(KafkaTemplate<Long, AccountDeactivationEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "bank.loan-security.events", groupId = "kefir-loan-fraud-group")
  public void consume(LoanRequestAuditEvent event) {
    Long customerId = event.customerId();
    Long accountId = event.accountId();
    Instant now = event.timestamp();

    loanActivityMap.compute(
        customerId,
        (id, timestamps) -> {
          if (timestamps == null) {
            timestamps = new ArrayList<>();
          }

          timestamps.removeIf(t -> t.isBefore(now.minus(TIME_WINDOW)));
          timestamps.add(now);

          if (timestamps.size() > MAX_TRANSACTIONS) {
            log.warn(
                "[FRAUD ALERT] Customer {} executed {} loan requests in less than a minute with"
                    + " account {}. Last amount requested ${}",
                id,
                accountId,
                timestamps.size(),
                event.requestedAmount());

            produceAccountDeactivationEvent(accountId);

          } else {
            log.info(
                "[Fraud Check OK] Customer {} with account {}: {}/{} transactions in the current"
                    + " window.",
                customerId,
                event.accountId(),
                timestamps.size(),
                MAX_TRANSACTIONS);
          }

          return timestamps;
        });
  }

  private void produceAccountDeactivationEvent(Long accountId) {
    log.warn("[FRAUD ALERT] Account {} in process of deactivation", accountId);

    String reason = "Account exceeded loan request limit";

    AccountDeactivationEvent accountDeactEvent =
        new AccountDeactivationEvent(UUID.randomUUID(), accountId, reason);

    kafkaTemplate.send("bank.account-security.events", accountId, accountDeactEvent);
  }
}
