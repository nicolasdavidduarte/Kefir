package com.kefir.services;

import com.kefir.entities.Loan;
import com.kefir.entities.LoanInstallment;
import com.kefir.entities.User;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.LoanInstallmentRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  private final LoanInstallmentRepository loanInstallmentRepository;
  private final AuthService authService;
  private UserService userService;

  public LoanInstallmentService(
      LoanInstallmentRepository loanInstallmentRepository,
      AuthService authService,
      UserService userService) {
    this.loanInstallmentRepository = loanInstallmentRepository;
    this.authService = authService;
    this.userService = userService;
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
    User user = userService.getById(authService.getCurrentUserId());
    LoanInstallment loanInstallment =
        LoanInstallment.createNew(loan, number, loanTotalAmount, user);

    loanInstallmentRepository.save(loanInstallment);
  }
}
