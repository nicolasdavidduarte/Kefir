package com.kefir.repositories;

import com.kefir.entities.Currency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  Optional<Currency> findByIsoCode(String isoCode);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @Query(value = "select c from Currency c where c.id = :id")
  Optional<Currency> findByIdWithDetails(@Param("id") Integer id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<Currency> findAllByOrderByIdAsc();
}
