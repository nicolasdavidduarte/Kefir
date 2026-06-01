package com.kefir.web.controllers;

import com.kefir.infrastructure.security.AuthService;
import com.kefir.web.dtos.AuthResponse;
import com.kefir.web.dtos.LoginRequest;
import com.kefir.web.dtos.RefreshRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Tag(name = "User authorization services", description = "APIs for user authorization")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest request) {
    return authService.login(request.username(), request.password());
  }

  @PostMapping("/refresh")
  public AuthResponse refresh(@RequestBody RefreshRequest request) {
    return authService.refresh(request.refreshToken());
  }
}
