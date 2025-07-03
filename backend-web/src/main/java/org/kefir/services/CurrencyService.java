package org.kefir.services;

import java.util.List;
import java.util.Optional;
import org.kefir.entities.Currency;
import org.kefir.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

  private final CurrencyRepository currencyRepository;

  public CurrencyService(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  public List<Currency> findAll() {
    return currencyRepository.findAll();
  }

  public Optional<Currency> findById(Long id) {
    return currencyRepository.findById(id);
  }
}
