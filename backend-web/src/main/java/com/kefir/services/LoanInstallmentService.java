package com.kefir.services;

import com.kefir.entities.Loan;
import com.kefir.entities.LoanInstallment;
import com.kefir.enums.AmortizationTypeName;
import com.kefir.repositories.LoanInstallmentRepository;
import com.kefir.services.loanInstallment.AmortizationCalculator;
import com.kefir.web.dtos.InstallmentData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  private final LoanInstallmentRepository loanInstallmentRepository;
  private final Map<AmortizationTypeName, AmortizationCalculator> calculators;

  public LoanInstallmentService(
      LoanInstallmentRepository loanInstallmentRepository,
      List<AmortizationCalculator> calculators) {
    this.loanInstallmentRepository = loanInstallmentRepository;

    this.calculators =
        calculators.stream()
            .collect(
                Collectors.toUnmodifiableMap(AmortizationCalculator::getType, Function.identity()));
  }

  public List<LoanInstallment> createInstallmentsSchedule(Loan loan) {
    AmortizationCalculator calculator = calculators.get(loan.getAmortizationType().getName());

    if (calculator == null) {
      throw new IllegalArgumentException(
          "Unsupported amortization type: " + loan.getAmortizationType().getName());
    }

    List<InstallmentData> schedule =
        calculator.generateSchedule(
            loan.getMonthlyInterestRate(),
            loan.getPrincipalAmount(),
            loan.getNumberOfInstallments());

    List<LoanInstallment> loanInstallments = new ArrayList<>();

    for (InstallmentData i : schedule) {

      LoanInstallment installment =
          LoanInstallment.createNew(
              loan,
              i.getNumber(),
              i.getPrincipalAmount(),
              i.getInterestAmount(),
              i.getTotalAmount(),
              i.getRemainingBalance(),
              loan.getOpeningDate().plusMonths(i.getNumber()),
              loan.getUser());

      loanInstallments.add(installment);
    }

    return loanInstallmentRepository.saveAll(loanInstallments);
  }
}
