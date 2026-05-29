package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.CustomerCreationException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.messaging.SnsPublisher;
import com.kefir.repositories.LoanRepository;
import com.kefir.web.dtos.LoanRequest;
import com.kefir.web.dtos.LoanResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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
  private final AuxAuthService auxAuthService;
  private final MeterRegistry registry;
  private final SnsPublisher snsPublisher;
  private final CustomerService customerService;
  private final LoanTypeService loanTypeService;
  private final CurrencyService currencyService;

  @Autowired
  public LoanService(
      SnsPublisher snsPublisher,
      LoanRepository loanRepository,
      AuxAuthService auxAuthService,
      MeterRegistry registry,
      LoanInstallmentService loanInstallmentService,
      CustomerService customerService,
      LoanTypeService loanTypeService,
      CurrencyService currencyService) {
    this.snsPublisher = snsPublisher;
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.auxAuthService = auxAuthService;
    this.loanInstallmentService = loanInstallmentService;
    this.customerService = customerService;
    this.loanTypeService = loanTypeService;
    this.currencyService = currencyService;
  }

  @Cacheable("loans")
  public List<Loan> getAll() {
    return loanRepository.findAll();
  }

  @Observed(name = "loan.service.get")
  public Optional<Loan> getById(Long id) {
    log.info("Loan search for id: {}", id);
    return loanRepository.findById(id);
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

      return LoanResponse.builder()
          .id(loanSaved.getId())
          .customer(loanSaved.getCustomer().getId())
          .loanType(loanSaved.getLoanType().getName())
          .totalOperationAmount(loanSaved.getTotalOperationAmount())
          .openingDate(loanSaved.getOpeningDate())
          .currency(loanSaved.getCurrency().getIsoCode())
          .expirationDate(loanSaved.getExpirationDate())
          .numberOfInstallments(loanSaved.getNumberOfInstallments())
          .status(loanSaved.getStatus())
          .createdAt(loanSaved.getCreatedAt())
          .user(loanSaved.getUser().getUsername())
          .build();

    } catch (CustomerCreationException e) {
      registry.counter("loan.created", "status", "error").increment();
      throw e;
    }
  }

  public LoanResponse createGerman() {
    return LoanResponse.builder().build();
  }

  public LoanResponse createAmerican() {
    return LoanResponse.builder().build();
  }

  @Transactional
  public void deleteLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
    loanRepository.delete(loan);

    log.info("Loan successfully deleted: {}", loan);
  }

  private Loan createLoan(LoanRequest loanRequest) {

    CoreUser user = auxAuthService.getUserFromAuth();

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
