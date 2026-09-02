package com.kefir.infrastructure.kafka;

import com.kefir.events.LoanRequestAuditEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FraudDetectionConsumer {

  private static final Logger log = LoggerFactory.getLogger(FraudDetectionConsumer.class);

  private final Map<Long, List<Instant>> loanActivityMap = new ConcurrentHashMap<>();

  private static final int MAX_TRANSACTIONS = 2;
  private static final Duration TIME_WINDOW = Duration.ofMinutes(1);

  @KafkaListener(topics = "bank.loan-security.events", groupId = "kefir-loan-fraud-group")
  public void consume(LoanRequestAuditEvent event) {
    Long customerId = event.customerId();
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
                "[FRAUD ALERT] Customer {} executed {} loan requests in less than a minute. Last"
                    + " amount requested ${}",
                id,
                timestamps.size(),
                event.requestedAmount());
          } else {
            log.info(
                "[Fraud Check OK] Customer {}: {}/{} transactions in the current window.",
                customerId,
                timestamps.size(),
                MAX_TRANSACTIONS);
          }

          return timestamps;
        });
  }
}
