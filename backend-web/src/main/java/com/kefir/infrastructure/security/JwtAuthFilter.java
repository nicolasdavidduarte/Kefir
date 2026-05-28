package com.kefir.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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

    if (header != null && header.startsWith("Bearer ")) {
      final String token = header.substring(7);

      try {
        final String username = jwtService.extractUsername(token);
        final List<String> roles = jwtService.extractRoles(token);

        final List<GrantedAuthority> authorities =
            roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        if (username != null && jwtService.isTokenValid(token, username)) {
          final UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
        // 1. Hand the exception to the GlobalExceptionHandler
        resolver.resolveException(request, response, null, e);
        return;
      } catch (Exception e) {
        SecurityContextHolder.clearContext();
        if (log.isDebugEnabled())
          log.debug("Security context cleared due to exception: {}", e.getMessage());
        if (log.isErrorEnabled())
          log.error("Unexpected error during authentication: {}", e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }
}
