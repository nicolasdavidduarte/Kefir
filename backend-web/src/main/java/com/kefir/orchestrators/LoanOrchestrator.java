package com.kefir.orchestrators;

import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.CustomerNotValidException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.services.CustomerService;
import com.kefir.services.LoanService;
import com.kefir.web.dtos.LoanRequest;
import com.kefir.web.dtos.LoanResponse;
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
    return loanService
        .findById(loanId)
        .map(this::toLoanDataDTO)
        .orElseThrow(() -> new LoanNotFoundException(loanId));
  }

  public Loan createLoan(LoanRequest loanRequest) {
    Customer customer = customerService.findById(loanRequest.getCustomer());
    if (customer.getStatus() != CustomerStatus.ACTIVE)
      throw new CustomerNotValidException("The customer is not allowed for a new loan");

    return switch (loanRequest.getLoanType()) {
      case 1 -> loanService.createFrench(loanRequest);
      case 2 -> loanService.createGerman();
      case 3 -> loanService.createAmerican();
      default -> throw new IllegalStateException("Unexpected value: " + loanRequest.getLoanType());
    };
  }

  private LoanResponse toLoanDataDTO(Loan loan) {
    return LoanResponse.builder()
        .id(loan.getId())
        .customer(loan.getCustomer().getId())
        .loanType(loan.getLoanType().getId())
        .totalOperationAmount(loan.getTotalOperationAmount())
        .openingDate(loan.getOpeningDate())
        .currency(loan.getCurrency().getId())
        .expirationDate(loan.getExpirationDate())
        .numberOfInstallments(loan.getNumberOfInstallments())
        .status(loan.getStatus())
        .updatedAt(loan.getUpdatedAt())
        .coreUser(loan.getUser().getId())
        .build();
  }
}
