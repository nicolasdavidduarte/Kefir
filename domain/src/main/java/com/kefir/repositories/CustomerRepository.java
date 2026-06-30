package com.kefir.repositories;

import com.kefir.entities.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findAllByOrderByIdAsc();

  @Query("""
    SELECT c
    FROM Customer c
    WHERE LOWER(c.fullname) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(c.documentNumber) LIKE LOWER(CONCAT('%', :query, '%'))
""")
  List<Customer> findByFullnameOrDocumentNumber(@Param("query") String query);
}
