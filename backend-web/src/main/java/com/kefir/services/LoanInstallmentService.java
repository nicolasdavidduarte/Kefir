package com.kefir.services;

import com.kefir.entities.LoanInstallment;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  public LoanInstallment createInstallments(Double loanTotalAmount, Integer numberOfInstallments) {
    return new LoanInstallment();
  }
}
