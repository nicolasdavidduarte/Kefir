package com.kefir.loanreport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCheckListener implements JobExecutionListener {

  @PersistenceContext private EntityManager em;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    try {
      em.createNativeQuery("SELECT 1").getSingleResult();
      System.out.println("✅ Database connection verified");
    } catch (Exception e) {
      System.err.println("❌ ERROR: Cannot connect to the database");
      throw new IllegalStateException("Database check failed", e);
    }
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    // optional
  }
}
