package org.kefir.mappers;

import org.kefir.DTOs.LoanDTO;
import org.kefir.entities.Loan;

public class LoanMapper {

  public static Loan fromDTO(LoanDTO dto) {
    Loan loan = new Loan();
    loan.setId(dto.getId());
    loan.setCustomer(dto.getCustomer());
    loan.setLoanType(dto.getLoanType());
    loan.setTotalOperationAmount(dto.getTotalOperationAmount());
    loan.setOpeningDate(dto.getOpeningDate());
    loan.setCurrency(dto.getCurrency());
    loan.setExpirationDate(dto.getExpirationDate());
    loan.setTotalTermDays(dto.getTotalTermDays());
    loan.setClosedDate(dto.getClosedDate());
    loan.setClosedCode(dto.getClosedCode());
    loan.setNextInstallmentDate(dto.getNextInstallmentDate());
    loan.setStatus(dto.getStatus());
    loan.setLastModificationDate(dto.getLastModificationDate());
    loan.setCoreUser(dto.getCoreUser());
    return loan;
  }
}
