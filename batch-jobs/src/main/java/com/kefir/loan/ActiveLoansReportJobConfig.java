package com.kefir.loan;

import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import jakarta.persistence.EntityManagerFactory;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
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
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ActiveLoansReportJobConfig {

  public static final String[] CSV_COLUMN_NAMES =
      new String[] {
        "loanId", "customerId", "status", "principal", "outstanding", "dueDate", "daysPastDue"
      };

  public static final String CSV_HEADER = String.join(",", CSV_COLUMN_NAMES);

  @Value("${kefir.batch.reports.directory:./reports}")
  private String reportsDirectory;

  private final DatabaseCheckListener databaseCheckListener;

  public ActiveLoansReportJobConfig(DatabaseCheckListener databaseCheckListener) {
    this.databaseCheckListener = databaseCheckListener;
  }

  @Bean
  public Job loanReportJob(JobRepository jobRepository, Step loanReportStep) {
    return new JobBuilder("loanReportJob", jobRepository)
        .start(loanReportStep)
        .listener(databaseCheckListener)
        .build();
  }

  @Bean
  public Step loanReportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      JpaPagingItemReader<Loan> reader,
      ItemProcessor<Loan, LoanReportLine> processor,
      FlatFileItemWriter<LoanReportLine> writer) {

    return new StepBuilder("loanReportStep", jobRepository)
        .<Loan, LoanReportLine>chunk(10)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Loan> loanReader(
      EntityManagerFactory entityManagerFactory,
      @Value("#{jobParameters['statusFilter']}") String statusFilter) {

    String status =
        (statusFilter != null && !statusFilter.isBlank())
            ? statusFilter.trim().toUpperCase()
            : "ACTIVE";

    JpaPagingItemReaderBuilder<Loan> builder =
        new JpaPagingItemReaderBuilder<Loan>()
            .name("loanReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(10);

    if ("ALL".equalsIgnoreCase(status)) {
      builder.queryString("SELECT l FROM Loan l JOIN FETCH l.customer ORDER BY l.id");
    } else {
      LoanStatus loanStatus;
      try {
        loanStatus = LoanStatus.valueOf(status);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Invalid statusFilter parameter: '"
                + status
                + "'. Valid values are: "
                + Arrays.toString(LoanStatus.values())
                + " or ALL",
            e);
      }
      builder
          .queryString(
              "SELECT l FROM Loan l JOIN FETCH l.customer WHERE l.status = :status ORDER BY l.id")
          .parameterValues(Map.of("status", loanStatus));
    }

    return builder.build();
  }

  @Bean
  @StepScope
  public ItemProcessor<Loan, LoanReportLine> loanProcessor(
      @Value("#{jobParameters['reportDate']}") String reportDateParam) {
    LocalDate reportDate = parseReportDate(reportDateParam);
    return new LoanReportProcessor(reportDate);
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<LoanReportLine> loanFileWriter(
      @Value("#{jobParameters['reportDate']}") String reportDateParam,
      @Value("#{jobParameters['statusFilter']}") String statusFilter,
      @Value("#{jobParameters['outputPath']}") String outputPath) {

    LocalDate reportDate = parseReportDate(reportDateParam);
    String dateStr = reportDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String effectiveStatus =
        (statusFilter != null && !statusFilter.isBlank())
            ? statusFilter.trim().toUpperCase()
            : "ACTIVE";

    String fileName = effectiveStatus + "_LOANS_REPORT_" + dateStr + ".csv";
    File targetFile = resolveOutputFile(outputPath, fileName, reportsDirectory);

    if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
      targetFile.getParentFile().mkdirs();
    }

    BeanWrapperFieldExtractor<LoanReportLine> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(CSV_COLUMN_NAMES);

    DelimitedLineAggregator<LoanReportLine> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor);

    return new FlatFileItemWriterBuilder<LoanReportLine>()
        .name("loanCsvWriter")
        .resource(new FileSystemResource(targetFile))
        .append(false)
        .shouldDeleteIfExists(true)
        .lineAggregator(lineAggregator)
        .headerCallback(writer -> writer.write(CSV_HEADER))
        .build();
  }

  public static File resolveOutputFile(
      String outputPath, String fileName, String defaultDirectory) {
    if (outputPath != null && !outputPath.isBlank()) {
      File file = new File(outputPath);
      if (outputPath.endsWith(".csv") || (!file.isDirectory() && file.getName().contains("."))) {
        return file;
      }
      return new File(file, fileName);
    }
    return new File(defaultDirectory, fileName);
  }

  public static LocalDate parseReportDate(String dateStr) {
    if (dateStr == null || dateStr.isBlank()) {
      return LocalDate.now();
    }
    String trimmed = dateStr.trim();
    try {
      if (trimmed.length() >= 10 && trimmed.charAt(4) == '-' && trimmed.charAt(7) == '-') {
        return LocalDate.parse(trimmed.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
      } else if (trimmed.length() == 8 && !trimmed.contains("-")) {
        return LocalDate.parse(trimmed, DateTimeFormatter.ofPattern("yyyyMMdd"));
      } else {
        return LocalDate.parse(trimmed);
      }
    } catch (DateTimeParseException e) {
      return LocalDate.now();
    }
  }

  public static class DailyReportJobParametersIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
      JobParametersBuilder builder =
          parameters != null ? new JobParametersBuilder(parameters) : new JobParametersBuilder();

      if (parameters == null || parameters.getString("reportDate") == null) {
        String reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        builder.addString("reportDate", reportDate);
      }
      if (parameters == null || parameters.getString("statusFilter") == null) {
        builder.addString("statusFilter", "ACTIVE");
      }

      return builder.addLong("run.id", System.currentTimeMillis()).toJobParameters();
    }
  }
}
