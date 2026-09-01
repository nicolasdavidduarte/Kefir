package com.kefir.repositories;

import com.kefir.entities.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @EntityGraph(
      attributePaths = {"personType", "documentType", "customerType", "createdBy", "updatedBy"})
  @Query(value = "select c from Customer c where c.id = :id")
  Optional<Customer> findByIdWithDetails(@Param("id") Long id);

  @EntityGraph(
      attributePaths = {"personType", "documentType", "customerType", "createdBy", "updatedBy"})
  List<Customer> findAllByOrderByIdAsc(Pageable pageable);

  @EntityGraph(
      attributePaths = {"personType", "documentType", "customerType", "createdBy", "updatedBy"})
  @Query(
"""
    SELECT c
    FROM Customer c
    WHERE LOWER(c.fullname) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(c.documentNumber) LIKE LOWER(CONCAT('%', :query, '%'))
""")
  List<Customer> findByFullnameOrDocumentNumber(@Param("query") String query);
}
