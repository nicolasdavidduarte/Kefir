package com.kefir.web.controllers;

import com.kefir.services.PersonTypeService;
import com.kefir.web.dtos.personType.PersonTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personType")
@Tag(name = "Person types services", description = "APIs for person types")
public class PersonTypeController {

  private final PersonTypeService personTypeService;

  public PersonTypeController(PersonTypeService personTypeService) {
    this.personTypeService = personTypeService;
  }

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<PersonTypeResponse> getAll() {
    return personTypeService.getAllWithResponse();
  }

  // Endpoint to retrieve a single record by ID
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public PersonTypeResponse getById(@PathVariable Integer id) {
    return personTypeService.getByIdWithResponse(id);
  }
}
