package com.kefir.loanreport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseCheckListener implements JobExecutionListener {

  @PersistenceContext private EntityManager em;

  @Override
  public void beforeJob(@NonNull JobExecution jobExecution) {
    try {
      em.createNativeQuery("SELECT 1").getSingleResult();
      if (log.isInfoEnabled()) {
        log.info("✅ Database connection verified");
      }
    } catch (DataAccessException e) {
      if (log.isErrorEnabled()) {
        log.error("❌ ERROR: Cannot connect to the database");
      }
      throw new IllegalStateException("Database check failed", e);
    }
  }

  @Override
  public void afterJob(@NonNull JobExecution jobExecution) {
    // optional
  }
}
