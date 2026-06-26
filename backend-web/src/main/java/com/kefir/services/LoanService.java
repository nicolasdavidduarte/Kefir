package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.CustomerStatus;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.LoanRepository;
import com.kefir.services.loanInstallment.LoanInstallmentService;
import com.kefir.web.dtos.loan.LoanRequest;
import com.kefir.web.dtos.loan.LoanResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoanService {

  private final LoanRepository loanRepository;
  private final LoanInstallmentService loanInstallmentService;
  private final AuthService authService;
  private final UserService userService;
  private final MeterRegistry registry;
  private final CustomerService customerService;
  private final LoanTypeService loanTypeService;
  private final CurrencyService currencyService;
  private final AmortizationTypeService amortizationTypeService;

  @Autowired
  public LoanService(
      LoanRepository loanRepository,
      AuthService authService,
      MeterRegistry registry,
      LoanInstallmentService loanInstallmentService,
      CustomerService customerService,
      LoanTypeService loanTypeService,
      CurrencyService currencyService,
      UserService userService,
      AmortizationTypeService amortizationTypeService) {
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.authService = authService;
    this.loanInstallmentService = loanInstallmentService;
    this.customerService = customerService;
    this.loanTypeService = loanTypeService;
    this.currencyService = currencyService;
    this.userService = userService;
    this.amortizationTypeService = amortizationTypeService;
  }

  @Cacheable("loans")
  public List<LoanResponse> getAll() {
    List<LoanResponse> loans =
        loanRepository.findAllByOrderByIdAsc().stream()
            .map(LoanResponse::fromEntity)
            .collect(Collectors.toCollection(ArrayList::new));

    if (loans.isEmpty()) {
      throw new ApiException(ErrorCode.LOANS_NOT_FOUND);
    }

    return loans;
  }

  @Observed(name = "loan.service.get")
  public Loan getById(Long id) {
    return loanRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));
  }

  @Observed(name = "loan.service.get")
  public LoanResponse getByIdWithResponse(Long id) {
    Loan loan =
        loanRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));
    return LoanResponse.fromEntity(loan);
  }

  @Transactional
  public void delete(Long id) {
    Loan loan =
        loanRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));
    loanRepository.delete(loan);

    log.info("Loan successfully deleted: {}", loan);
  }

  @Transactional
  public LoanResponse create(LoanRequest loanRequest) {
    // TODO: Add interest rate mode (fixed or variable)

    Customer customer = customerService.getById(loanRequest.customerId());

    if (customer.getStatus() != CustomerStatus.ACTIVE)
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID);

    try {

      OffsetDateTime now = OffsetDateTime.now();

      User user = userService.getById(authService.getCurrentUserId());

      LoanType loanType = loanTypeService.getByNameIgnoringCase(loanRequest.loanType());

      BigDecimal annualInterestRate = loanType.getAnnualInterestRate();
      BigDecimal monthlyInterestRate =
          annualInterestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

      AmortizationType amortizationType =
          amortizationTypeService.getByNameIgnoringCase(loanRequest.amortizationType());

      Currency currency = currencyService.getByIsoCode(loanRequest.currencyIsoCode());

      Loan loan =
          Loan.builder()
              .customer(customer)
              .loanType(loanType)
              .amortizationType(amortizationType)
              .principalAmount(loanRequest.principalAmount())
              .interestAmount(BigDecimal.ZERO)
              .totalOperationAmount(BigDecimal.ZERO)
              .annualInterestRate(annualInterestRate)
              .monthlyInterestRate(monthlyInterestRate)
              .openingDate(now)
              .currency(currency)
              .numberOfInstallments(loanRequest.numberOfInstallments())
              .expirationDate(now.plusMonths(loanRequest.numberOfInstallments()))
              .status(LoanStatus.PENDING)
              .user(user)
              .externalId(loanRequest.externalId())
              .createdAt(now)
              .updatedAt(now)
              .build();

      Loan loanSaved = loanRepository.saveAndFlush(loan);

      List<LoanInstallment> loanInstallments =
          loanInstallmentService.createInstallmentsSchedule(loanSaved);

      BigDecimal totalOperationAmount =
          loanInstallments.stream()
              .map(LoanInstallment::getTotalAmount)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      BigDecimal interestAmount =
          loanInstallments.stream()
              .map(LoanInstallment::getInterestAmount)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      loanSaved.setTotalOperationAmount(totalOperationAmount);
      loanSaved.setInterestAmount(interestAmount);

      registry.counter("loan.created", "status", "success").increment();

      log.info("Loan successfully created - id: {}", loanSaved);

      return LoanResponse.fromEntity(loanSaved);

    } catch (Exception e) {
      registry.counter("loan.created", "status", "error").increment();
      throw e;
    }
  }
}
