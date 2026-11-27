package com.kefir.loan;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;

class ActiveLoansReportJobConfigTest {

  @Test
  @DisplayName("parseReportDate parses ISO format yyyy-MM-dd correctly")
  void shouldParseIsoDate() {
    LocalDate date = ActiveLoansReportJobConfig.parseReportDate("2026-11-27");
    assertThat(date).isEqualTo(LocalDate.of(2026, 11, 27));
  }

  @Test
  @DisplayName("parseReportDate parses compact format yyyyMMdd correctly")
  void shouldParseCompactDate() {
    LocalDate date = ActiveLoansReportJobConfig.parseReportDate("20261127");
    assertThat(date).isEqualTo(LocalDate.of(2026, 11, 27));
  }

  @Test
  @DisplayName("parseReportDate falls back to today on null or invalid input")
  void shouldFallbackToTodayOnInvalidInput() {
    LocalDate today = LocalDate.now();
    assertThat(ActiveLoansReportJobConfig.parseReportDate(null)).isEqualTo(today);
    assertThat(ActiveLoansReportJobConfig.parseReportDate("")).isEqualTo(today);
    assertThat(ActiveLoansReportJobConfig.parseReportDate("invalid-date")).isEqualTo(today);
  }

  @Test
  @DisplayName("resolveOutputFile handles explicit csv file path")
  void shouldResolveExplicitCsvPath() {
    File file =
        ActiveLoansReportJobConfig.resolveOutputFile(
            "/tmp/custom_output.csv", "DEFAULT.csv", "./reports");
    assertThat(file.getPath()).isEqualTo("/tmp/custom_output.csv");
  }

  @Test
  @DisplayName("resolveOutputFile handles directory path")
  void shouldResolveDirectoryPath() {
    File file =
        ActiveLoansReportJobConfig.resolveOutputFile(
            "/tmp/output_dir", "REPORT_20261127.csv", "./reports");
    assertThat(file.getPath())
        .isEqualTo(new File("/tmp/output_dir", "REPORT_20261127.csv").getPath());
  }

  @Test
  @DisplayName("resolveOutputFile falls back to default directory")
  void shouldFallbackToDefaultDirectory() {
    File file =
        ActiveLoansReportJobConfig.resolveOutputFile(null, "REPORT_20261127.csv", "./reports");
    assertThat(file.getPath()).isEqualTo(new File("./reports", "REPORT_20261127.csv").getPath());
  }

  @Test
  @DisplayName(
      "DailyReportJobParametersIncrementer populates default reportDate, statusFilter, and run.id")
  void shouldIncrementParametersWithDefaults() {
    ActiveLoansReportJobConfig.DailyReportJobParametersIncrementer incrementer =
        new ActiveLoansReportJobConfig.DailyReportJobParametersIncrementer();

    JobParameters params = incrementer.getNext(new JobParameters());
    assertThat(params.getString("reportDate")).isNotNull();
    assertThat(params.getString("statusFilter")).isEqualTo("ACTIVE");
    assertThat(params.getLong("run.id")).isNotNull();

    // When parameters already provided, existing values are preserved
    JobParameters existing =
        new JobParametersBuilder()
            .addString("reportDate", "2026-10-15")
            .addString("statusFilter", "CLOSED")
            .toJobParameters();

    JobParameters next = incrementer.getNext(existing);
    assertThat(next.getString("reportDate")).isEqualTo("2026-10-15");
    assertThat(next.getString("statusFilter")).isEqualTo("CLOSED");
    assertThat(next.getLong("run.id")).isNotNull();
  }
}
