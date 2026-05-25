package com.kefir.services;

import com.kefir.entities.LoanInstallment;
import com.kefir.repositories.LoanInstallmentRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  private final LoanInstallmentRepository loanInstallmentRepository;

  public LoanInstallmentService(LoanInstallmentRepository loanInstallmentRepository) {
    this.loanInstallmentRepository = loanInstallmentRepository;
  }

  public void createInstallmentsSchedule(
      Long loanId, Double loanTotalAmount, Integer numberOfInstallments, Integer loanType) {
    int i;
    for (i = 1; i <= numberOfInstallments; i++) {

      createInstallment(i, loanId, loanTotalAmount, loanType);
    }
  }

  private void createInstallment(
      int number, Long loanId, Double loanTotalAmount, Integer loanType) {
    LoanInstallment loanInstallment =
        LoanInstallment.createNew(loanId, number, BigDecimal.valueOf(loanTotalAmount));

    loanInstallmentRepository.save(loanInstallment);
  }
}
