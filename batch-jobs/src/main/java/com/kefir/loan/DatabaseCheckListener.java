package com.kefir.loan;

import com.kefir.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseCheckListener implements JobExecutionListener {

  @PersistenceContext private EntityManager em;

  private static final Integer SYSTEM_USER = 1;

  @Override
  public void beforeJob(@NonNull JobExecution jobExecution) {
    try {
      em.createNativeQuery("SELECT 1").getSingleResult();
      if (log.isInfoEnabled()) {
        log.info("Database connection verified");
      }
    } catch (DataAccessException e) {
      if (log.isErrorEnabled()) {
        log.error("ERROR: Cannot connect to the database");
      }
      throw new IllegalStateException("Database check failed", e);
    }

    User systemUser = em.find(User.class, SYSTEM_USER);
    jobExecution.getExecutionContext().put("SYSTEM_USER", systemUser);
  }
}
