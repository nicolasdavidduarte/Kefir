package com.kefir.infrastructure.security;

import com.kefir.entities.CoreUser;
import com.kefir.entities.RefreshToken;
import com.kefir.repositories.CoreUserRepository;
import com.kefir.web.DTOs.AuthResponse;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final CoreUserRepository userRepository;

  public AuthService(
      AuthenticationManager authManager,
      JwtService jwtService,
      RefreshTokenService refreshTokenService,
      CoreUserRepository userRepository) {
    this.authManager = authManager;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
    this.userRepository = userRepository;
  }

  public AuthResponse login(String username, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final CoreUser user = userRepository.findByUsername(username).orElseThrow();

    final List<String> roles =
        user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    final String accessToken = jwtService.generateToken(username, roles);

    final RefreshToken refreshToken = refreshTokenService.createToken(user);

    return new AuthResponse(accessToken, refreshToken.getToken());
  }

  public AuthResponse refresh(String refreshToken) {

    final RefreshToken oldToken = refreshTokenService.verify(refreshToken);

    final CoreUser user = oldToken.getUser();

    refreshTokenService.revoke(oldToken);

    final RefreshToken newToken = refreshTokenService.createToken(user);

    final List<String> roles =
        user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    final String newAccessToken = jwtService.generateToken(user.getUsername(), roles);

    return new AuthResponse(newAccessToken, newToken.getToken());
  }
}
