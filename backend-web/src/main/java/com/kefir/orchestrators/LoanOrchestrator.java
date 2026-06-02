package com.kefir.orchestrators;

import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.CustomerNotValidException;
import com.kefir.services.CustomerService;
import com.kefir.services.LoanService;
import com.kefir.web.dtos.loan.LoanRequest;
import com.kefir.web.dtos.loan.LoanResponse;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanOrchestrator {
  private final LoanService loanService;
  private final CustomerService customerService;

  @Observed(name = "loan.orchestrated.get")
  public LoanResponse getLoanData(Long loanId) {
    Loan loan = loanService.getById(loanId);
    return LoanResponse.fromEntity(loan);
  }

  public LoanResponse createLoan(LoanRequest loanRequest) {
    Customer customer = customerService.getById(loanRequest.customerId());
    if (customer.getStatus() != CustomerStatus.ACTIVE) throw new CustomerNotValidException();

    return switch (loanRequest.loanType()) {
      case FRENCH -> loanService.createFrench(loanRequest);
      case GERMAN -> loanService.createGerman();
      case AMERICAN -> loanService.createAmerican();
    };
  }
}
