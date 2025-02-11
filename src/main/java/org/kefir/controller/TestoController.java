package org.kefir.controller;

import org.kefir.entity.Testo;
import org.kefir.service.TestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testo")
public class TestoController {

    @Autowired
    private final TestoService testoService;

    public TestoController(TestoService testoService) {
        this.testoService = testoService;
    }

    // Endpoint to retrieve all records from the testo table
    @GetMapping
    public List<Testo> getAll() {
        return testoService.findAll();
    }

    // Endpoint to retrieve a single record by ID
    @GetMapping("/{id}")
    public Optional<Testo> getById(@PathVariable Long id) {
        return testoService.findById(id);
    }
}