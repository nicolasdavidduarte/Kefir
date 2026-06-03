package com.kefir.services;

import com.kefir.entities.Loan;
import com.kefir.entities.LoanInstallment;
import com.kefir.enums.AmortizationTypeName;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.LoanInstallmentRepository;
import com.kefir.services.loanInstallment.AmortizationCalculator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LoanInstallmentService {

  private final LoanInstallmentRepository loanInstallmentRepository;
  private final AuthService authService;
  private UserService userService;
  private final Map<AmortizationTypeName, AmortizationCalculator> calculators;

  public LoanInstallmentService(
      LoanInstallmentRepository loanInstallmentRepository,
      AuthService authService,
      UserService userService,
      List<AmortizationCalculator> calculators) {
    this.loanInstallmentRepository = loanInstallmentRepository;
    this.authService = authService;
    this.userService = userService;

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

    List<LoanInstallment> schedule = calculator.generateSchedule(loan);

    return loanInstallmentRepository.saveAll(schedule);
  }
}
