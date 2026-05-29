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

  public List<Currency> getAll() {
    return currencyRepository.findAll();
  }

  public Currency getById(Integer id) {
    return currencyRepository.findById(id).orElseThrow(CurrencyNotFoundException::new);
  }

  public Currency getByIsoCode(CurrencyIsoCodes isoCode) {
    return currencyRepository
        .findByIsoCode(isoCode.toString())
        .orElseThrow(CurrencyNotFoundException::new);
  }
}
