package com.kefir.services;

import com.kefir.entities.Currency;
import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.exceptions.CurrencyNotFoundException;
import com.kefir.repositories.CurrencyRepository;
import com.kefir.web.dtos.currency.CurrencyResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

  private final CurrencyRepository currencyRepository;

  public CurrencyService(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  public List<CurrencyResponse> getAll() {
    return currencyRepository.findAll().stream().map(CurrencyResponse::fromEntity).toList();
  }

  public CurrencyResponse getById(Integer id) {
    Currency currency = currencyRepository.findById(id).orElseThrow(CurrencyNotFoundException::new);
    return CurrencyResponse.fromEntity(currency);
  }

  public Currency getByIsoCode(CurrencyIsoCodes isoCode) {
    return currencyRepository
        .findByIsoCode(isoCode.toString())
        .orElseThrow(CurrencyNotFoundException::new);
  }

  public CurrencyResponse getByIsoCodeWithResponse(CurrencyIsoCodes isoCode) {
    Currency currency =
        currencyRepository
            .findByIsoCode(isoCode.name())
            .orElseThrow(CurrencyNotFoundException::new);

    return CurrencyResponse.fromEntity(currency);
  }
}
