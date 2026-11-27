package com.kefir.loan;

import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class LoanReportProcessor implements ItemProcessor<Loan, LoanReportLine> {

  private final LocalDate reportDate;

  public LoanReportProcessor(LocalDate reportDate) {
    this.reportDate = reportDate != null ? reportDate : LocalDate.now();
  }

  @Override
  public LoanReportLine process(@NonNull Loan loan) {
    Customer customer = loan.getCustomer();
    Long customerId = customer != null ? customer.getId() : null;

    BigDecimal principal = loan.getPrincipalAmount();
    BigDecimal outstanding = loan.getRemainingBalance();

    OffsetDateTime expirationDate = loan.getExpirationDate();
    LocalDate dueDate = expirationDate != null ? expirationDate.toLocalDate() : null;

    long daysPastDue = 0L;
    if (dueDate != null
        && reportDate.isAfter(dueDate)
        && outstanding != null
        && outstanding.compareTo(BigDecimal.ZERO) > 0) {
      daysPastDue = ChronoUnit.DAYS.between(dueDate, reportDate);
    }

    String status = loan.getStatus() != null ? loan.getStatus().name() : null;

    return LoanReportLine.builder()
        .loanId(loan.getId())
        .customerId(customerId)
        .status(status)
        .principal(principal)
        .outstanding(outstanding)
        .dueDate(dueDate)
        .daysPastDue(daysPastDue)
        .build();
  }

  public LocalDate getReportDate() {
    return reportDate;
  }
}
