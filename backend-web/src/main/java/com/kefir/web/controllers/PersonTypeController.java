package com.kefir.web.controllers;

import com.kefir.entities.PersonType;
import com.kefir.services.PersonTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personType")
public class PersonTypeController {

  private final PersonTypeService personTypeService;

  public PersonTypeController(PersonTypeService personTypeService) {
    this.personTypeService = personTypeService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<PersonType> getAll() {
    return personTypeService.findAll();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  public Optional<PersonType> getById(@PathVariable Integer id) {
    return personTypeService.findById(id);
  }
}
