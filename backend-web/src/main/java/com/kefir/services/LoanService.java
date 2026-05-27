package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.CustomerCreationException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.messaging.SnsPublisher;
import com.kefir.repositories.LoanRepository;
import com.kefir.repositories.LoanTypeRepository;
import com.kefir.web.dtos.LoanRequest;
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
  private final LoanTypeRepository loanTypeRepository;
  private final CurrencyService currencyService;

  @Autowired
  public LoanService(
      SnsPublisher snsPublisher,
      LoanRepository loanRepository,
      AuxAuthService auxAuthService,
      MeterRegistry registry,
      LoanInstallmentService loanInstallmentService,
      CustomerService customerService,
      LoanTypeRepository loanTypeRepository,
      CurrencyService currencyService) {
    this.snsPublisher = snsPublisher;
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.auxAuthService = auxAuthService;
    this.loanInstallmentService = loanInstallmentService;
    this.customerService = customerService;
    this.loanTypeRepository = loanTypeRepository;
    this.currencyService = currencyService;
  }

  @Cacheable("loans")
  public List<Loan> findAll() {
    return loanRepository.findAll();
  }

  @Observed(name = "loan.service.get")
  public Optional<Loan> findById(Long id) {
    log.info("Loan search for id: {}", id);
    return loanRepository.findById(id);
  }

  @Transactional
  public Loan createFrench(LoanRequest loanRequest) {

    // TODO: Add interest rate mode (fixed or variable)

    try {

      Loan loanSaved = createLoan(loanRequest);

      createLoanInstallment(loanSaved);

      registry.counter("loan.created", "status", "success").increment();

      log.info("Loan successfully created - id: {}", loanSaved);

      snsPublisher.publishLoanCreated(loanSaved.getId(), loanSaved.getTotalOperationAmount());

      return loanSaved;
    } catch (Exception e) {
      registry.counter("loan.created", "status", "error").increment();
      throw new CustomerCreationException(e);
    }
  }

  public Loan createGerman() {
    return new Loan();
  }

  public Loan createAmerican() {
    return new Loan();
  }

  @Transactional
  public void deleteLoan(Long id) {
    Loan loan = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException(id));
    loanRepository.delete(loan);

    log.info("Loan successfully deleted: {}", loan);
  }

  @Transactional
  public void updateLoan(Long loanId, LoanRequest loanRequest) {
    Loan loan =
        loanRepository
            .findById(loanId)
            .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));

    CoreUser user = auxAuthService.retrieveUserFromAuth();

    loan.setTotalOperationAmount(loanRequest.getTotalOperationAmount());
    loan.setUpdatedAt(OffsetDateTime.now());
    loan.setUser(user);

    loanRepository.save(loan);

    log.info("Loan successfully updated: {}", loan);
  }

  private Loan createLoan(LoanRequest loanRequest) {

    CoreUser user = auxAuthService.retrieveUserFromAuth();
    Customer customer = customerService.fetchById(loanRequest.getCustomer());
    LoanType loanType =
        loanTypeRepository
            .findById(loanRequest.getLoanType())
            .orElseThrow(() -> new RuntimeException("Loan type not found"));
    Currency currency = currencyService.findById(loanRequest.getCurrency());

    Loan loan =
        Loan.builder()
            .customer(customer)
            .loanType(loanType)
            .totalOperationAmount(loanRequest.getTotalOperationAmount())
            .openingDate(loanRequest.getOpeningDate())
            .currency(currency)
            .numberOfInstallments(loanRequest.getNumberOfInstallments())
            .status(LoanStatus.ACTIVE)
            .user(user)
            .build();

    return loanRepository.save(loan);
  }

  private void createLoanInstallment(Loan loan) {
    loanInstallmentService.createInstallmentsSchedule(loan);
  }
}
