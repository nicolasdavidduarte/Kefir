package com.kefir.repositories;

import com.kefir.entities.Customer;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Override
  @EntityGraph(
      attributePaths = {"personType", "documentType", "customerType", "createdBy", "updatedBy"})
  @NonNull Optional<Customer> findById(@NonNull Long Id);

  @EntityGraph(
      attributePaths = {"personType", "documentType", "customerType", "createdBy", "updatedBy"})
  List<Customer> findAllByOrderByIdAsc();

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
