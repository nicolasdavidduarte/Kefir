package com.kefir.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);

      try {
        String username = jwtService.extractUsername(token);
        List<String> roles = jwtService.extractRoles(token);

        List<GrantedAuthority> authorities =
            roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        if (username != null && jwtService.isTokenValid(token, username)) {
          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception e) {
      }
    }

    filterChain.doFilter(request, response);
  }
}
