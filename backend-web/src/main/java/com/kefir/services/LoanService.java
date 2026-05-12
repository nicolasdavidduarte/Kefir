package com.kefir.services;

import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.CustomerCreationException;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.config.metrics.LoanActiveState;
import com.kefir.infrastructure.messaging.SnsPublisher;
import com.kefir.repositories.LoanRepository;
import com.kefir.web.DTOs.LoanRequest;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoanService {

  private final LoanRepository loanRepository;
  private final AuxAuthService auxAuthService;
  private final MeterRegistry registry;
  private final LoanActiveState state;
  private final SnsPublisher snsPublisher;

  @Autowired
  public LoanService(
      SnsPublisher snsPublisher,
      LoanRepository loanRepository,
      AuxAuthService auxAuthService,
      MeterRegistry registry,
      LoanActiveState state) {
    this.snsPublisher = snsPublisher;
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.state = state;
    this.auxAuthService = auxAuthService;
  }

  public List<Loan> findAll() {
    return loanRepository.findAll();
  }

  @Observed(name = "loan.service.get")
  public Optional<Loan> findById(Long id) {
    log.info("Loan search for id: {}", id);
    return loanRepository.findById(id);
  }

  @Transactional
  public Loan create(LoanRequest loanRequest) {

    Integer user = auxAuthService.retrieveUserIdFromAuth();

    try {
      Loan loan =
          Loan.builder()
              .customer(loanRequest.getCustomer())
              .loanType(loanRequest.getLoanType())
              .totalOperationAmount(loanRequest.getTotalOperationAmount())
              .openingDate(loanRequest.getOpeningDate())
              .currency(loanRequest.getCurrency())
              .closedCode(loanRequest.getClosedCode())
              .closedDate(loanRequest.getClosedDate())
              .nextInstallmentDate(loanRequest.getNextInstallmentDate())
              .status(LoanStatus.ACTIVE.getId())
              .lastModificationDate(LocalDate.now())
              .coreUser(user)
              .build();

      Loan loanSaved = loanRepository.save(loan);

      registry.counter("loan.created", "status", "success").increment();

      if (loanSaved.getStatus().equals(LoanStatus.ACTIVE.getId())) state.increment();

      log.info("Loan successfully created - id: {}", loanSaved);

      snsPublisher.publishLoanCreated(loan.getId(), loan.getTotalOperationAmount());

      return loanSaved;
    } catch (Exception e) {
      registry.counter("loan.created", "status", "error").increment();
      throw new CustomerCreationException(e);
    }
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

    Integer user = auxAuthService.retrieveUserIdFromAuth();

    loan.setTotalOperationAmount(loanRequest.getTotalOperationAmount());
    loan.setLastModificationDate(LocalDate.now());
    loan.setCoreUser(user);

    loanRepository.save(loan);

    log.info("Loan successfully updated: {}", loan);
  }
}
