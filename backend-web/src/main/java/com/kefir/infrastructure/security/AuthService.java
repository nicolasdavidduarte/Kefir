package com.kefir.infrastructure.security;

import com.kefir.entities.User;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.repositories.UserRepository;
import com.kefir.web.dtos.auth.AuthResponse;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;

  public AuthService(
      AuthenticationManager authManager, JwtService jwtService, UserRepository userRepository) {
    this.authManager = authManager;
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  public AuthResponse login(String username, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final User user = userRepository.findByUsername(username).orElseThrow();

    if (!user.isEnabled()) throw new ApiException(ErrorCode.USER_NOT_VALID);

    final List<String> roles =
        user.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

    final String accessToken = jwtService.generateToken(user.getId(), username, roles);

    return new AuthResponse(accessToken, OffsetDateTime.now());
  }

  public Integer getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    AuthenticatedUser principal = (AuthenticatedUser) auth.getPrincipal();

    return principal.id();
  }
}
