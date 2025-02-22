package org.kefir.services;

import org.kefir.DTOs.LoanDTO;
import org.kefir.entities.Loan;
import org.kefir.repositories.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Transactional
    public Loan create(LoanDTO loanDTO){
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

        return loanRepository.save(loan);
    }

    @Transactional
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        loanRepository.delete(loan);
    }

    @Transactional
    public void updateLoan(LoanDTO loanDTO) {
        int id = loanDTO.getId();
        Loan loan = loanRepository.findById((long) id)
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