package com.kefir.loan;

import static org.assertj.core.api.Assertions.assertThat;

import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoanReportProcessorTest {

  @Test
  @DisplayName("Should map loan fields to LoanReportLine accurately")
  void shouldMapLoanToLoanReportLine() {
    LocalDate reportDate = LocalDate.of(2026, 11, 27);
    LoanReportProcessor processor = new LoanReportProcessor(reportDate);

    Customer customer = Customer.builder().id(42L).build();
    OffsetDateTime expirationDate = OffsetDateTime.of(2026, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC);

    Loan loan =
        Loan.builder()
            .id(100L)
            .customer(customer)
            .status(LoanStatus.ACTIVE)
            .principalAmount(new BigDecimal("15000.00"))
            .remainingBalance(new BigDecimal("12000.50"))
            .expirationDate(expirationDate)
            .build();

    LoanReportLine line = processor.process(loan);

    assertThat(line).isNotNull();
    assertThat(line.getLoanId()).isEqualTo(100L);
    assertThat(line.getCustomerId()).isEqualTo(42L);
    assertThat(line.getStatus()).isEqualTo("ACTIVE");
    assertThat(line.getPrincipal()).isEqualByComparingTo("15000.00");
    assertThat(line.getOutstanding()).isEqualByComparingTo("12000.50");
    assertThat(line.getDueDate()).isEqualTo(LocalDate.of(2026, 12, 31));
    assertThat(line.getDaysPastDue()).isEqualTo(0L);
  }

  @Test
  @DisplayName("Should calculate positive daysPastDue when loan is past due with positive balance")
  void shouldCalculateDaysPastDueWhenOverdue() {
    LocalDate reportDate = LocalDate.of(2026, 11, 27);
    LoanReportProcessor processor = new LoanReportProcessor(reportDate);

    OffsetDateTime expirationDate = OffsetDateTime.of(2026, 11, 20, 0, 0, 0, 0, ZoneOffset.UTC);

    Loan loan =
        Loan.builder()
            .id(101L)
            .status(LoanStatus.ACTIVE)
            .principalAmount(new BigDecimal("5000.00"))
            .remainingBalance(new BigDecimal("2500.00"))
            .expirationDate(expirationDate)
            .build();

    LoanReportLine line = processor.process(loan);

    assertThat(line).isNotNull();
    assertThat(line.getDueDate()).isEqualTo(LocalDate.of(2026, 11, 20));
    assertThat(line.getDaysPastDue()).isEqualTo(7L);
  }

  @Test
  @DisplayName("Should return 0 daysPastDue when loan is past due but remaining balance is zero")
  void shouldReturnZeroDaysPastDueWhenBalanceZero() {
    LocalDate reportDate = LocalDate.of(2026, 11, 27);
    LoanReportProcessor processor = new LoanReportProcessor(reportDate);

    OffsetDateTime expirationDate = OffsetDateTime.of(2026, 11, 10, 0, 0, 0, 0, ZoneOffset.UTC);

    Loan loan =
        Loan.builder()
            .id(102L)
            .status(LoanStatus.CLOSED)
            .principalAmount(new BigDecimal("5000.00"))
            .remainingBalance(BigDecimal.ZERO)
            .expirationDate(expirationDate)
            .build();

    LoanReportLine line = processor.process(loan);

    assertThat(line).isNotNull();
    assertThat(line.getDaysPastDue()).isEqualTo(0L);
  }

  @Test
  @DisplayName("Should safely handle null customer and null expirationDate")
  void shouldHandleNullOptionalFields() {
    LocalDate reportDate = LocalDate.of(2026, 11, 27);
    LoanReportProcessor processor = new LoanReportProcessor(reportDate);

    Loan loan =
        Loan.builder()
            .id(103L)
            .customer(null)
            .status(null)
            .principalAmount(new BigDecimal("1000.00"))
            .remainingBalance(new BigDecimal("1000.00"))
            .expirationDate(null)
            .build();

    LoanReportLine line = processor.process(loan);

    assertThat(line).isNotNull();
    assertThat(line.getCustomerId()).isNull();
    assertThat(line.getStatus()).isNull();
    assertThat(line.getDueDate()).isNull();
    assertThat(line.getDaysPastDue()).isEqualTo(0L);
  }
}
