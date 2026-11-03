package com.kefir.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityExceptionHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");

    final String json =
        "{\"error\": \"Forbidden\", \"message\": \"User has no access allowed to this endpoint\","
            + " \"status\": 403}";
    response.getWriter().write(json);
  }
}
