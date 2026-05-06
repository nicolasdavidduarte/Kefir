package com.kefir.services;

import com.kefir.entities.LoanType;
import com.kefir.repositories.LoanTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LoanTypeService {

  private final LoanTypeRepository loanTypeRepository;

  public LoanTypeService(LoanTypeRepository loanTypeRepository) {
    this.loanTypeRepository = loanTypeRepository;
  }

  public List<LoanType> findAll() {
    return loanTypeRepository.findAll();
  }

  public Optional<LoanType> findById(Long id) {
    return loanTypeRepository.findById(id);
  }
}
