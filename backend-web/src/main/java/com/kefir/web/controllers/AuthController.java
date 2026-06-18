package com.kefir.web.controllers;

import com.kefir.infrastructure.security.AuthService;
import com.kefir.web.dtos.auth.AuthResponse;
import com.kefir.web.dtos.auth.LoginRequest;
import com.kefir.web.dtos.auth.RefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User authorization services", description = "APIs for user authorization")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(
      summary = "Login",
      security = {})
  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest request) {
    return authService.login(request.username(), request.password());
  }

  @PostMapping("/refresh")
  public AuthResponse refresh(@RequestBody RefreshRequest request) {
    return authService.refresh(request.refreshToken());
  }
}
