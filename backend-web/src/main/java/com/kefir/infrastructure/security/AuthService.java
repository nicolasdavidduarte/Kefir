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

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final CoreUserRepository userRepository;

  public AuthService(
      AuthenticationManager authenticationManager,
      JwtService jwtService,
      RefreshTokenService refreshTokenService,
      CoreUserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
    this.userRepository = userRepository;
  }

  public AuthResponse login(String username, String password) {

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    CoreUser user = userRepository.findByUsername(username).orElseThrow();

    List<String> roles = user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    String accessToken = jwtService.generateToken(username, roles);

    RefreshToken refreshToken = refreshTokenService.createToken(user);

    return new AuthResponse(accessToken, refreshToken.getToken());
  }

  public AuthResponse refresh(String refreshToken) {

    RefreshToken oldToken = refreshTokenService.verify(refreshToken);

    CoreUser user = oldToken.getUser();

    refreshTokenService.revoke(oldToken);

    RefreshToken newToken = refreshTokenService.createToken(user);

    List<String> roles = user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    String newAccessToken = jwtService.generateToken(user.getUsername(), roles);

    return new AuthResponse(newAccessToken, newToken.getToken());
  }
}
