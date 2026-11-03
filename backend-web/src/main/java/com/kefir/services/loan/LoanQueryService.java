package com.kefir.services.loan;

import com.kefir.entities.Loan;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.repositories.LoanRepository;
import com.kefir.web.dtos.loan.LoanResponse;
import io.micrometer.observation.annotation.Observed;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanQueryService {

  private final LoanRepository loanRepository;

  public LoanQueryService(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Transactional(readOnly = true)
  public List<LoanResponse> getAll(Pageable pageable) {
    return loanRepository.findAllByOrderByIdAsc(pageable).stream()
        .map(LoanResponse::fromEntity)
        .toList();
  }

  @Observed(name = "loan.service.get")
  @Transactional(readOnly = true)
  public Loan getById(Long id) {
    return loanRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));
  }

  @Observed(name = "loan.service.get")
  @Transactional(readOnly = true)
  public LoanResponse getByIdWithResponse(Long id) {
    Loan loan =
        loanRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));
    return LoanResponse.fromEntity(loan);
  }

  @Transactional(readOnly = true)
  public List<LoanResponse> getAllByAccountId(Long accountId) {
    return loanRepository.findAllByAccountId(accountId).stream()
        .map(LoanResponse::fromEntity)
        .toList();
  }
}
