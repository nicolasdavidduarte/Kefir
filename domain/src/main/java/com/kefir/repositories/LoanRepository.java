package com.kefir.repositories;

import com.kefir.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
  Integer countByStatus(Integer status);
}
