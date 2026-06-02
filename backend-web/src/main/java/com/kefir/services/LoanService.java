package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.CustomerCreationException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.messaging.SnsPublisher;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.LoanRepository;
import com.kefir.web.dtos.loan.LoanRequest;
import com.kefir.web.dtos.loan.LoanResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import java.time.OffsetDateTime;
import java.util.List;
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
  private final SnsPublisher snsPublisher;
  private final CustomerService customerService;
  private final LoanTypeService loanTypeService;
  private final CurrencyService currencyService;

  @Autowired
  public LoanService(
      SnsPublisher snsPublisher,
      LoanRepository loanRepository,
      AuthService authService,
      MeterRegistry registry,
      LoanInstallmentService loanInstallmentService,
      CustomerService customerService,
      LoanTypeService loanTypeService,
      CurrencyService currencyService,
      UserService userService) {
    this.snsPublisher = snsPublisher;
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.authService = authService;
    this.loanInstallmentService = loanInstallmentService;
    this.customerService = customerService;
    this.loanTypeService = loanTypeService;
    this.currencyService = currencyService;
    this.userService = userService;
  }

  @Cacheable("loans")
  public List<LoanResponse> getAll() {
    return loanRepository.findAll().stream().map(LoanResponse::fromEntity).toList();
  }

  @Observed(name = "loan.service.get")
  public Loan getById(Long id) {
    return loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
  }

  @Observed(name = "loan.service.get")
  public LoanResponse getByIdWithResponse(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
    return LoanResponse.fromEntity(loan);
  }

  @Transactional
  public LoanResponse createFrench(LoanRequest loanRequest) {

    // TODO: Add interest rate mode (fixed or variable)

    try {

      Loan loanSaved = createLoan(loanRequest);

      createLoanInstallment(loanSaved);

      registry.counter("loan.created", "status", "success").increment();

      log.info("Loan successfully created - id: {}", loanSaved);

      snsPublisher.publishLoanCreated(loanSaved.getId(), loanSaved.getTotalOperationAmount());

      return LoanResponse.fromEntity(loanSaved);

    } catch (CustomerCreationException e) {
      registry.counter("loan.created", "status", "error").increment();
      throw e;
    }
  }

  // TODO: German Loan
  public LoanResponse createGerman() {
    Loan loan = new Loan();
    return LoanResponse.fromEntity(loan);
  }

  // TODO: American Loan
  public LoanResponse createAmerican() {
    Loan loan = new Loan();
    return LoanResponse.fromEntity(loan);
  }

  @Transactional
  public void deleteLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
    loanRepository.delete(loan);

    log.info("Loan successfully deleted: {}", loan);
  }

  private Loan createLoan(LoanRequest loanRequest) {

    User user = userService.getById(authService.getCurrentUserId());

    Customer customer = customerService.getById(loanRequest.customerId());

    LoanType loanType = loanTypeService.getByNameIgnoringCase(loanRequest.loanType());

    Currency currency = currencyService.getByIsoCode(loanRequest.currencyIsoCode());

    Loan loan =
        Loan.builder()
            .customer(customer)
            .loanType(loanType)
            .totalOperationAmount(loanRequest.totalOperationAmount())
            .openingDate(OffsetDateTime.now())
            .currency(currency)
            .numberOfInstallments(loanRequest.numberOfInstallments())
            .expirationDate(OffsetDateTime.now().plusMonths(loanRequest.numberOfInstallments()))
            .status(LoanStatus.PENDING)
            .user(user)
            .externalId(loanRequest.externalId())
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

    return loanRepository.save(loan);
  }

  private void createLoanInstallment(Loan loan) {
    loanInstallmentService.createInstallmentsSchedule(loan);
  }
}
