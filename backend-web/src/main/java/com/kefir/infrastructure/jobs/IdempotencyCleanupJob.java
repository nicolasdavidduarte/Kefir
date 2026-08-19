package com.kefir.infrastructure.jobs;

import com.kefir.repositories.IdempotentRequestRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyCleanupJob {

  private final IdempotentRequestRepository repository;

  public IdempotencyCleanupJob(IdempotentRequestRepository repository) {
    this.repository = repository;
  }

  @Scheduled(cron = "0 0 3 * * ?")
  @Transactional
  public void cleanupOldRequests() {
    OffsetDateTime threshold = OffsetDateTime.now().minusDays(7);
    repository.deleteByCreatedAtBefore(threshold);
  }
}
