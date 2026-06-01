package com.kefir.web.controllers;

import com.kefir.services.UserService;
import com.kefir.web.dtos.UserRequest;
import com.kefir.web.dtos.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAll() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(@PathVariable Integer id) {
    return ResponseEntity.ok(userService.getByIdWithResponse(id));
  }

  @PostMapping()
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
    return ResponseEntity.ok(userService.create(request));
  }

  @PatchMapping("/activate/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Map<String, String>> activateUser(@PathVariable Integer id) {
    userService.activate(id);

    return ResponseEntity.ok(Map.of("message", "User successfully activated!"));
  }

  @PatchMapping("/deactivate/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Integer id) {
    userService.deactivate(id);

    return ResponseEntity.ok(Map.of("message", "User successfully deactivated!"));
  }
}
