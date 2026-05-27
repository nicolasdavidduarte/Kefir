package com.kefir.web.controllers;

import com.kefir.entities.Currency;
import com.kefir.services.CurrencyService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

  private final CurrencyService currencyService;

  public CurrencyController(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<Currency> getAll() {
    return currencyService.findAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public Currency getById(@PathVariable Integer id) {
    return currencyService.findById(id);
  }
}
