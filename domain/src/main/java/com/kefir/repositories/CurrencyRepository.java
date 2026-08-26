package com.kefir.repositories;

import com.kefir.entities.Currency;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  Optional<Currency> findByIsoCode(String isoCode);

  @Override
  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @NonNull Optional<Currency> findById(@NonNull Integer id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<Currency> findAllByOrderByIdAsc();
}
