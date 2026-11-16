package com.kefir.loan;

import com.kefir.entities.Loan;
import com.kefir.entities.User;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AccrueLoansJobConfig {

  private final DatabaseCheckListener databaseCheckListener;
  private final Integer DAYS_365 = 365;
  private static final MathContext FINANCIAL_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);

  public AccrueLoansJobConfig(DatabaseCheckListener databaseCheckListener) {
    this.databaseCheckListener = databaseCheckListener;
  }

  @Bean
  public Job accrueLoansJob(JobRepository jobRepository, Step accrueLoansStep) {
    return new JobBuilder("accrueLoansJob", jobRepository)
        .start(accrueLoansStep)
        .listener(databaseCheckListener)
        .build();
  }

  @Bean
  public Step accrueLoansStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      JpaPagingItemReader<Loan> reader,
      ItemProcessor<Loan, Loan> processor,
      JpaItemWriter<Loan> writer) {

    return new StepBuilder("accrueLoansStep", jobRepository)
        .<Loan, Loan>chunk(10)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public ItemProcessor<Loan, Loan> loanAccrualProcessor() {
    return loan -> {
      User user =
          (User)
              StepSynchronizationManager.getContext()
                  .getStepExecution()
                  .getJobExecution()
                  .getExecutionContext()
                  .get("SYSTEM_USER");
      BigDecimal air =
          loan.getAnnualInterestRate().divide(BigDecimal.valueOf(100), FINANCIAL_CONTEXT);
      BigDecimal perDiem = air.divide(BigDecimal.valueOf(DAYS_365), FINANCIAL_CONTEXT);
      BigDecimal interestAmount = loan.getRemainingBalance().multiply(perDiem);
      loan.accrueInterest(user, interestAmount);
      return loan;
    };
  }

  @Bean
  @StepScope
  public JpaItemWriter<Loan> loanWriter(EntityManagerFactory emf) {
    return new JpaItemWriterBuilder<Loan>().entityManagerFactory(emf).build();
  }
}
