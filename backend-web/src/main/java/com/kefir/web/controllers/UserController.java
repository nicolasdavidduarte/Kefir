package com.kefir.web.controllers;

import com.kefir.services.UserService;
import com.kefir.web.dtos.EntityStatusUpdate;
import com.kefir.web.dtos.UserRequest;
import com.kefir.web.dtos.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User services", description = "APIs for system users")
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

  @PostMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Map<String, String>> updateStatus(@PathVariable Integer id, @RequestBody EntityStatusUpdate status) {
    String response = userService.updateStatus(id, status);

    return ResponseEntity.ok(Map.of("message", response));
  }
}
