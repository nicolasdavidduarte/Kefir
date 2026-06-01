package com.kefir.web.controllers;

import com.kefir.entities.CoreUser;
import com.kefir.services.CoreUserService;
import java.util.List;
import java.util.Optional;

import com.kefir.web.dtos.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class CoreUserController {

  private final CoreUserService coreUserService;

  public CoreUserController(CoreUserService coreUserService) {
    this.coreUserService = coreUserService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAll() {
    return coreUserService.getAll();
  }

  @GetMapping("/{id}")
  public Optional<CoreUser> getById(@PathVariable Integer id) {
    return coreUserService.getById(id);
  }
}
