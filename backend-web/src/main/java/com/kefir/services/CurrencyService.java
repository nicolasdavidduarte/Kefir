package com.kefir.services;

import com.kefir.entities.Currency;
import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.exceptions.CurrencyNotFoundException;
import com.kefir.repositories.CurrencyRepository;
import java.util.List;
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

  public Currency findById(Integer id) {
    return currencyRepository
        .findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
  }

  public Currency fetchByIsoCode(CurrencyIsoCodes isoCode) {
    return currencyRepository
        .findByIsoCode(isoCode.toString())
        .orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
  }
}
