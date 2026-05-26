package com.kefir.services;

import com.kefir.entities.Currency;
import com.kefir.repositories.CurrencyRepository;
import java.util.List;
import java.util.Optional;
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

  public Optional<Currency> findById(Integer id) {
    return currencyRepository.findById(id);
  }
}
