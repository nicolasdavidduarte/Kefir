package com.kefir.repositories;

import com.kefir.entities.Loan;
import com.kefir.enums.LoanStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
  Integer countByStatus(LoanStatus status);

  @EntityGraph(
      attributePaths = {
        "customer",
        "account",
        "loanType",
        "amortizationType",
        "currency",
        "createdBy",
        "updatedBy"
      })
  List<Loan> findAllByOrderByIdAsc(Pageable pageable);

  @EntityGraph(
      attributePaths = {
        "customer",
        "account",
        "loanType",
        "amortizationType",
        "currency",
        "createdBy",
        "updatedBy"
      })
  List<Loan> findAllByAccountId(Long accountId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      """
      select l from Loan l where l.id = :id
      """)
  Optional<Loan> findByIdForUpdate(Long id);
}
