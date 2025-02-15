package org.kefir.controllers;

import org.kefir.entities.Currency;
import org.kefir.services.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public Optional<Currency> getById(@PathVariable Long id) {
        return currencyService.findById(id);
    }
}