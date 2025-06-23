package org.kefir.orchestrators;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kefir.DTOs.LoanDataDTO;
import org.kefir.entities.Loan;
import org.kefir.services.LoanService;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoanDataOrchestrator {
    private final LoanService loanService;

    public LoanDataDTO getLoanData(Long loanId) {
        return loanService.findById(loanId)
                .map(this::toLoanDataDTO)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));
    }

    private LoanDataDTO toLoanDataDTO(Loan loan) {
        return LoanDataDTO.builder()
                .id((long) loan.getId())
                .customer(loan.getCustomer())
                .loanType(loan.getLoanType())
                .totalOperationAmount(loan.getTotalOperationAmount())
                .openingDate(loan.getOpeningDate())
                .currency(loan.getCurrency())
                .expirationDate(loan.getExpirationDate())
                .totalTermDays(loan.getTotalTermDays())
                .closedDate(loan.getClosedDate())
                .closedCode(loan.getClosedCode())
                .nextInstallmentDate(loan.getNextInstallmentDate())
                .status(loan.getStatus())
                .lastModificationDate(loan.getLastModificationDate())
                .coreUser(loan.getCoreUser())
                .build();
    }

}
