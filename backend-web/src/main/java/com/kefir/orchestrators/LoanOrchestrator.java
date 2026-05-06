package com.kefir.orchestrators;

import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.CustomerNotValidException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.services.CustomerService;
import com.kefir.services.LoanService;
import com.kefir.web.DTOs.LoanRequest;
import com.kefir.web.DTOs.LoanResponse;
import io.micrometer.observation.annotation.Observed;
import java.time.LocalDate;
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
    return loanService
        .findById(loanId)
        .map(this::toLoanDataDTO)
        .orElseThrow(() -> new LoanNotFoundException(loanId));
  }

  public Loan createLoan(LoanRequest loan) {
    Customer customer = customerService.findById(loan.getCustomer());
    if (!customer.getStatus().equals(CustomerStatus.ACTIVE.getId()))
      throw new CustomerNotValidException("The customer is not allowed for a new loan");
    return loanService.create(loan);
  }

  private LoanResponse toLoanDataDTO(Loan loan) {
    return LoanResponse.builder()
        .id(loan.getId())
        .customer(loan.getCustomer())
        .loanType(loan.getLoanType())
        .totalOperationAmount(loan.getTotalOperationAmount())
        .openingDate(LocalDate.now())
        .currency(loan.getCurrency())
        .expirationDate(loan.getExpirationDate())
        .totalTermDays(loan.getTotalTermDays())
        .closedDate(loan.getClosedDate())
        .closedCode(loan.getClosedCode())
        .nextInstallmentDate(loan.getNextInstallmentDate())
        .status(loan.getStatus())
        .lastModificationDate(loan.getLastModificationDate())
        .coreUser(loan.getCoreUser())
        .build();
  }
}
