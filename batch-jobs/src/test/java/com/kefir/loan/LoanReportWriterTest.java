package com.kefir.loan;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

class LoanReportWriterTest {

  @TempDir File tempDir;

  @Test
  @DisplayName(
      "DelimitedLineAggregator with BeanWrapperFieldExtractor formats LoanReportLine correctly")
  void shouldAggregateFieldsCorrectly() {
    BeanWrapperFieldExtractor<LoanReportLine> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(ActiveLoansReportJobConfig.CSV_COLUMN_NAMES);

    DelimitedLineAggregator<LoanReportLine> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor);

    LoanReportLine line =
        LoanReportLine.builder()
            .loanId(1L)
            .customerId(10L)
            .status("ACTIVE")
            .principal(new BigDecimal("10000.00"))
            .outstanding(new BigDecimal("8500.25"))
            .dueDate(LocalDate.of(2027, 1, 15))
            .daysPastDue(0L)
            .build();

    String aggregated = lineAggregator.aggregate(line);
    assertThat(aggregated).isEqualTo("1,10,ACTIVE,10000.00,8500.25,2027-01-15,0");
  }

  @Test
  @DisplayName(
      "FlatFileItemWriter produces CSV with header and data rows, and is idempotent on rerun")
  void shouldWriteCsvWithHeaderAndBeIdempotent() throws Exception {
    File outputFile = new File(tempDir, "TEST_REPORT.csv");

    BeanWrapperFieldExtractor<LoanReportLine> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(ActiveLoansReportJobConfig.CSV_COLUMN_NAMES);

    DelimitedLineAggregator<LoanReportLine> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor);

    FlatFileItemWriter<LoanReportLine> writer =
        new FlatFileItemWriterBuilder<LoanReportLine>()
            .name("testLoanWriter")
            .resource(new FileSystemResource(outputFile))
            .append(false)
            .shouldDeleteIfExists(true)
            .lineAggregator(lineAggregator)
            .headerCallback(w -> w.write(ActiveLoansReportJobConfig.CSV_HEADER))
            .build();

    ExecutionContext executionContext = new ExecutionContext();
    writer.open(executionContext);

    LoanReportLine item1 =
        LoanReportLine.builder()
            .loanId(101L)
            .customerId(201L)
            .status("ACTIVE")
            .principal(new BigDecimal("1000.00"))
            .outstanding(new BigDecimal("500.00"))
            .dueDate(LocalDate.of(2026, 11, 20))
            .daysPastDue(7L)
            .build();

    LoanReportLine item2 =
        LoanReportLine.builder()
            .loanId(102L)
            .customerId(202L)
            .status("ACTIVE")
            .principal(new BigDecimal("2000.00"))
            .outstanding(new BigDecimal("2000.00"))
            .dueDate(LocalDate.of(2026, 12, 1))
            .daysPastDue(0L)
            .build();

    writer.write(new Chunk<>(List.of(item1, item2)));
    writer.close();

    List<String> lines = Files.readAllLines(outputFile.toPath());
    assertThat(lines).hasSize(3);
    assertThat(lines.get(0))
        .isEqualTo("loanId,customerId,status,principal,outstanding,dueDate,daysPastDue");
    assertThat(lines.get(1)).isEqualTo("101,201,ACTIVE,1000.00,500.00,2026-11-20,7");
    assertThat(lines.get(2)).isEqualTo("102,202,ACTIVE,2000.00,2000.00,2026-12-01,0");

    // Re-run test to verify idempotency (file is overwritten, not duplicated)
    writer.open(new ExecutionContext());
    writer.write(new Chunk<>(List.of(item1)));
    writer.close();

    List<String> rerunLines = Files.readAllLines(outputFile.toPath());
    assertThat(rerunLines).hasSize(2);
    assertThat(rerunLines.get(0)).isEqualTo(ActiveLoansReportJobConfig.CSV_HEADER);
    assertThat(rerunLines.get(1)).isEqualTo("101,201,ACTIVE,1000.00,500.00,2026-11-20,7");
  }
}
