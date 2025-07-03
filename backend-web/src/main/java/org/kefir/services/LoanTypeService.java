package org.kefir.services;

import java.util.List;
import java.util.Optional;
import org.kefir.entities.LoanType;
import org.kefir.repositories.LoanTypeRepository;
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
