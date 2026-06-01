package com.kefir.infrastructure.security;

import com.kefir.entities.RefreshToken;
import com.kefir.entities.User;
import com.kefir.repositories.UserRepository;
import com.kefir.web.dtos.AuthResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;

  public AuthService(
      AuthenticationManager authManager,
      JwtService jwtService,
      RefreshTokenService refreshTokenService,
      UserRepository userRepository) {
    this.authManager = authManager;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
    this.userRepository = userRepository;
  }

  public AuthResponse login(String username, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final User user = userRepository.findByUsername(username).orElseThrow();

    final List<String> roles =
        user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    final String accessToken = jwtService.generateToken(user.getId(), username, roles);

    return new AuthResponse(accessToken, OffsetDateTime.now());
  }

  public AuthResponse refresh(String refreshToken) {

    final RefreshToken oldToken = refreshTokenService.verify(refreshToken);

    final User user = oldToken.getUser();

    refreshTokenService.revoke(oldToken);

    final RefreshToken newToken = refreshTokenService.createToken(user);

    final List<String> roles =
        user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    final String newAccessToken = jwtService.generateToken(user.getId(), user.getUsername(), roles);

    return new AuthResponse(newAccessToken, OffsetDateTime.now());
  }

  public Integer getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    AuthenticatedUser principal = (AuthenticatedUser) auth.getPrincipal();

    return principal.id();
  }

  public String getCurrentUsername() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    AuthenticatedUser principal = (AuthenticatedUser) auth.getPrincipal();

    return principal.username();
  }
}
