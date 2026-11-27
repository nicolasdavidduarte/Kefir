package com.kefir.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanReportLine {
  private Long loanId;
  private Long customerId;
  private String status;
  private BigDecimal principal;
  private BigDecimal outstanding;
  private LocalDate dueDate;
  private Long daysPastDue;
}
