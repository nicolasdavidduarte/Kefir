package org.kefir.controllers;

import org.kefir.entities.Status;
import org.kefir.services.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // Endpoint to retrieve all records from the person_type table
    @GetMapping
    public List<Status> getAll() {
        return statusService.findAll();
    }

    // Endpoint to retrieve a single record by ID
    @GetMapping("/{id}")
    public Optional<Status> getById(@PathVariable Long id) {
        return statusService.findById(id);
    }
}