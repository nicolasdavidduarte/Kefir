package com.kefir.services;

import com.kefir.entities.Currency;
import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
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

  public List<CurrencyResponse> getAllWithResponse() {
    return currencyRepository.findAllByOrderByIdAsc().stream()
        .map(CurrencyResponse::fromEntity)
        .toList();
  }

  public CurrencyResponse getByIdWithResponse(Integer id) {
    Currency currency =
        currencyRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
    return CurrencyResponse.fromEntity(currency);
  }

  public Currency getByIsoCode(CurrencyIsoCodes isoCode) {
    return currencyRepository
        .findByIsoCode(isoCode.name())
        .orElseThrow(() -> new ApiException(ErrorCode.CURRENCY_NOT_FOUND));
  }

  public CurrencyResponse getByIsoCodeWithResponse(CurrencyIsoCodes isoCode) {
    Currency currency =
        currencyRepository
            .findByIsoCode(isoCode.name())
            .orElseThrow(() -> new ApiException(ErrorCode.CURRENCY_NOT_FOUND));

    return CurrencyResponse.fromEntity(currency);
  }
}
