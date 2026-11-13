package com.kefir.loanreport;

import com.kefir.entities.Loan;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.job.parameters.JobParametersIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActiveLoansReportJobConfig {

  private final DatabaseCheckListener databaseCheckListener;

  public ActiveLoansReportJobConfig(DatabaseCheckListener databaseCheckListener) {
    this.databaseCheckListener = databaseCheckListener;
  }

  @Bean
  public Job loanReportJob(JobRepository jobRepository, Step loanReportStep) {
    return new JobBuilder("loanReportJob", jobRepository)
        .start(loanReportStep)
        .listener(databaseCheckListener)
        .incrementer(new DailyReportJobParametersIncrementer())
        .build();
  }

  @Bean
  public Step loanReportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      JpaPagingItemReader<Loan> reader,
      ItemProcessor<Loan, Loan> processor,
      FlatFileItemWriter<Loan> writer) {

    return new StepBuilder("loanReportStep", jobRepository)
        .<Loan, Loan>chunk(10)
        .transactionManager(transactionManager)
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
        .queryString("SELECT l FROM Loan l WHERE l.status = 'ACTIVE'")
        .pageSize(10)
        .build();
  }

  @Bean
  public ItemProcessor<Loan, Loan> loanProcessor() {
    return loan -> loan;
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<Loan> loanWriter(
      @Value("#{jobParameters['reportDate']}") String reportDate) {

    if (reportDate == null || reportDate.isBlank()) {
      reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    String filePath = "batch-jobs/reports/ACTIVE_LOANS_REPORT_" + reportDate + ".csv";

    return new FlatFileItemWriterBuilder<Loan>()
        .name("loanCsvWriter")
        .resource(new FileSystemResource(filePath))
        .delimited()
        .delimiter(",")
        .names(new String[] {"id", "customer", "totalOperationAmount", "openingDate", "status"})
        .headerCallback(writer -> writer.write("ID,Customer,Amount,Date,Status"))
        .build();
  }

  public static class DailyReportJobParametersIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
      String reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

      return new JobParametersBuilder(parameters != null ? parameters : new JobParameters())
          .addString("reportDate", reportDate)
          .addLong("run.id", System.currentTimeMillis())
          .toJobParameters();
    }
  }
}
