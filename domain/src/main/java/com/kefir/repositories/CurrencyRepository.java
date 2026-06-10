package com.kefir.repositories;

import com.kefir.entities.Currency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

  Optional<Currency> findByIsoCode(String isoCode);

  List<Currency> findAllByOrderByIdAsc();
}
