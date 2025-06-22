package org.kefir.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.kefir.DTOs.LoanDTO;
import org.kefir.entities.Loan;
import org.kefir.infrastructure.messaging.SnsPublisher;
import org.kefir.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

  private final LoanRepository loanRepository;
  public List<Loan> findAll() {
    return loanRepository.findAll();
  }
  public Optional<Loan> findById(Long id) {
    return loanRepository.findById(id);
  }
  private final SnsPublisher snsPublisher;

  @Autowired
  public LoanService(SnsPublisher snsPublisher, LoanRepository loanRepository) {
    this.snsPublisher = snsPublisher;
    this.loanRepository = loanRepository;
  }

  @Transactional
  public Loan create(LoanDTO loanDTO) {
    Loan loan = new Loan();

    loan.setCustomer(loanDTO.getCustomer());
    loan.setLoanType(loanDTO.getLoanType());
    loan.setTotalOperationAmount(loanDTO.getTotalOperationAmount());
    loan.setOpeningDate(loanDTO.getOpeningDate());
    loan.setCurrency(loanDTO.getCurrency());
    loan.setClosedCode(loanDTO.getClosedCode());
    loan.setClosedDate(loanDTO.getClosedDate());
    loan.setNextInstallmentDate(loanDTO.getNextInstallmentDate());
    loan.setStatus(loanDTO.getStatus());
    loan.setLastModificationDate(loanDTO.getLastModificationDate());
    loan.setCoreUser(loanDTO.getCoreUser());

    Loan loanSaved = loanRepository.save(loan);

    snsPublisher.publishLoanCreated(loan.getId(), loan.getTotalOperationAmount());

    return loanSaved;
  }

  @Transactional
  public void deleteLoan(Long id) {
    Loan loan =
        loanRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    loanRepository.delete(loan);
  }

  @Transactional
  public void updateLoan(LoanDTO loanDTO) {
    int id = loanDTO.getId();
    Loan loan =
        loanRepository
            .findById((long) id)
            .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

    loan.setCustomer(loanDTO.getCustomer());
    loan.setLoanType(loanDTO.getLoanType());
    loan.setTotalOperationAmount(loanDTO.getTotalOperationAmount());
    loan.setOpeningDate(loanDTO.getOpeningDate());
    loan.setCurrency(loanDTO.getCurrency());
    loan.setClosedCode(loanDTO.getClosedCode());
    loan.setClosedDate(loanDTO.getClosedDate());
    loan.setNextInstallmentDate(loanDTO.getNextInstallmentDate());
    loan.setStatus(loanDTO.getStatus());
    loan.setLastModificationDate(new Date());
    loan.setCoreUser(loanDTO.getCoreUser());

    loanRepository.save(loan);
  }
}
