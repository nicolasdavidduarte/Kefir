package org.kefir.loanreport;

import jakarta.persistence.EntityManagerFactory;
import org.kefir.entities.Loan;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LoanReportJobConfig {

  private final DatabaseCheckListener databaseCheckListener;

  public LoanReportJobConfig(DatabaseCheckListener databaseCheckListener) {
    this.databaseCheckListener = databaseCheckListener;
  }

  @Bean
  public Job loanReportJob(JobRepository jobRepository, Step loanReportStep) {
    return new JobBuilder("loanReportJob", jobRepository)
            .start(loanReportStep)
            .listener(databaseCheckListener)  // Asociamos el listener aquí
            .build();
  }

  @Bean
  public Step loanReportStep(
          JobRepository jobRepository,
          PlatformTransactionManager transactionManager,
          JpaPagingItemReader<Loan> reader,
          ItemProcessor<Loan, Loan> processor,
          ItemWriter<Loan> writer) {
    return new StepBuilder("loanReportStep", jobRepository)
            .<Loan, Loan>chunk(10, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
  }

  @Bean
  public JpaPagingItemReader<Loan> loanReader(EntityManagerFactory entityManagerFactory) {
    return new JpaPagingItemReaderBuilder<Loan>()
            .name("loanReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT l FROM Loan l")
            .pageSize(10)
            .build();
  }

  @Bean
  public ItemProcessor<Loan, Loan> loanProcessor() {
    return loan -> loan; // No hace nada por ahora
  }

  @Bean
  public ItemWriter<Loan> loanWriter() {
    return items -> {
      System.out.println(">>> Reporte de Préstamos:");
      for (Loan loan : items) {
        System.out.printf(
                "ID: %d | Cliente: %d | Monto: %.2f | Fecha: %s | Estado: %d%n",
                loan.getId(),
                loan.getCustomer(),
                loan.getTotalOperationAmount(),
                loan.getOpeningDate(),
                loan.getStatus());
      }
    };
  }
}
