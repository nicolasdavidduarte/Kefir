package com.kefir.web.controllers;

import com.kefir.entities.CoreUser;
import com.kefir.services.CoreUserService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coreUser")
public class CoreUserController {

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
