package org.kefir.controller;

import org.kefir.entity.CoreUser;
import org.kefir.service.CoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coreUser")
public class CoreUserController {

    @Autowired
    private final CoreUserService coreUserService;

    public CoreUserController(CoreUserService coreUserService) {
        this.coreUserService = coreUserService;
    }

    // Endpoint to retrieve all records from the core_user table
    @GetMapping
    public List<CoreUser> getAll() {
        return coreUserService.findAll();
    }

    // Endpoint to retrieve a single record by ID
    @GetMapping("/{id}")
    public Optional<CoreUser> getById(@PathVariable Long id) {
        return coreUserService.findById(id);
    }
}