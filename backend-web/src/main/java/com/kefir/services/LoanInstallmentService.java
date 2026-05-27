package com.kefir.services;

import com.kefir.entities.CoreUser;
import com.kefir.entities.Loan;
import com.kefir.entities.LoanInstallment;
import com.kefir.repositories.LoanInstallmentRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  private final LoanInstallmentRepository loanInstallmentRepository;
  private final AuxAuthService auxAuthService;

  public LoanInstallmentService(
      LoanInstallmentRepository loanInstallmentRepository, AuxAuthService auxAuthService) {
    this.loanInstallmentRepository = loanInstallmentRepository;
    this.auxAuthService = auxAuthService;
  }

  public void createInstallmentsSchedule(Loan loan) {
    int i;

    Integer numberOfInstallments = loan.getNumberOfInstallments();
    BigDecimal loanTotalAmount = loan.getTotalOperationAmount();

    for (i = 1; i <= numberOfInstallments; i++) {
      createInstallment(i, loan, loanTotalAmount);
    }
  }

  private void createInstallment(int number, Loan loan, BigDecimal loanTotalAmount) {
    CoreUser user = auxAuthService.retrieveUserFromAuth();
    LoanInstallment loanInstallment =
        LoanInstallment.createNew(loan, number, loanTotalAmount, user);

    loanInstallmentRepository.save(loanInstallment);
  }
}
