package com.kefir.infrastructure.security;

import com.kefir.entities.CoreUser;
import com.kefir.entities.RefreshToken;
import com.kefir.repositories.RefreshTokenRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository repository;

  public RefreshTokenService(RefreshTokenRepository repository) {
    this.repository = repository;
  }

  public RefreshToken createToken(CoreUser user) {
    RefreshToken token = new RefreshToken();
    token.setUser(user);
    token.setToken(UUID.randomUUID().toString());
    token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
    token.setRevoked(false);

    return repository.save(token);
  }

  public Optional<RefreshToken> findByToken(String token) {
    return repository.findByToken(token);
  }

  public RefreshToken verify(String token) {
    RefreshToken refreshToken =
        repository
            .findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    return refreshToken;
  }

  public void revoke(RefreshToken token) {
    token.setRevoked(true);
    repository.save(token);
  }
}
