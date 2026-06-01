package com.kefir.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final HandlerExceptionResolver resolver;

  public JwtAuthFilter(
      JwtService jwtService,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.jwtService = jwtService;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    final String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = header.substring(7);

    try {

      // Parse the JWT only once
      final Claims claims = jwtService.extractAllClaims(token);

      final String username = claims.getSubject();
      final Integer userId = claims.get("userId", Integer.class);

      if (username == null || username.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      }

      // Validate expiration
      final Date expiration = claims.getExpiration();
      final Date now = Date.from(Instant.now());

      if (expiration.before(now)) {
        throw new ExpiredJwtException(null, claims, "JWT token expired");
      }

      // Extract roles safely
      final Object rolesObject = claims.get("roles");

      final List<GrantedAuthority> authorities =
          rolesObject instanceof List<?> list
              ? list.stream()
                  .map(Object::toString)
                  .map(SimpleGrantedAuthority::new)
                  .map(GrantedAuthority.class::cast)
                  .toList()
              : List.of();

      AuthenticatedUser principal = new AuthenticatedUser(userId, username);

      final UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(principal, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(auth);

      if (log.isDebugEnabled()) {
        log.debug(
            "Authenticated user '{}' with roles {}. Token expires at {}",
            username,
            authorities,
            expiration);
      }

    } catch (ExpiredJwtException e) {

      SecurityContextHolder.clearContext();

      if (log.isDebugEnabled()) {
        log.debug("JWT token expired: {}", e.getMessage());
      }

      resolver.resolveException(request, response, null, e);
      return;

    } catch (SignatureException | MalformedJwtException e) {

      SecurityContextHolder.clearContext();

      if (log.isDebugEnabled()) {
        log.debug("Invalid JWT token: {}", e.getMessage());
      }

      resolver.resolveException(request, response, null, e);
      return;

    } catch (Exception e) {

      SecurityContextHolder.clearContext();

      log.error("Unexpected error during JWT authentication", e);

      resolver.resolveException(request, response, null, e);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
