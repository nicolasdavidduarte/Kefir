package com.kefir.web.controllers;

import com.kefir.enums.CurrencyIsoCodes;
import com.kefir.services.CurrencyService;
import com.kefir.web.dtos.currency.CurrencyResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currencies")
@Tag(name = "Currency services", description = "APIs for currencies")
public class CurrencyController {

  private final CurrencyService currencyService;

  public CurrencyController(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<CurrencyResponse> getAll() {
    return currencyService.getAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{isoCode}")
  public CurrencyResponse getByIsoCode(@PathVariable CurrencyIsoCodes isoCode) {
    return currencyService.getByIsoCodeWithResponse(isoCode);
  }
}
