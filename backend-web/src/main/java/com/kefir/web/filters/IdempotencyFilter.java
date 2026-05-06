package com.kefir.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kefir.enums.IdempotencyState;
import com.kefir.model.IdempotentRequest;
import com.kefir.repositories.IdempotentRequestRepository;
import com.kefir.web.utils.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

  private final IdempotentRequestRepository repository;
  private final ObjectMapper objectMapper =
      new ObjectMapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

  public IdempotencyFilter(IdempotentRequestRepository repository) {
    this.repository = repository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String IDEMPOTENCY_ERROR = "Idempotency Error";

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (request.getRequestURI().startsWith("/api/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    String key = request.getHeader("Idempotency-Key");

    if (key == null) {
      filterChain.doFilter(request, response);
      return;
    }

    // Wrap request and response
    CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);

    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    // Read body and calculate hash
    String body = wrappedRequest.getBody();

    String normalized = normalizeJson(body);
    String requestHash = hashRequest(normalized);

    // Search for key
    Optional<IdempotentRequest> existingOpt = repository.findByIdempotencyKey(key);

    if (existingOpt.isPresent()) {
      IdempotentRequest existing = existingOpt.get();

      if (IdempotencyState.COMPLETED.equals(existing.getState())) {

        if (!existing.getEndpoint().equals(request.getRequestURI())) {
          writeErrorResponse(
              response,
              HttpServletResponse.SC_CONFLICT,
              IDEMPOTENCY_ERROR,
              "Idempotency-Key used for different endpoint");
          response.flushBuffer();
          return;
        }

        if (!existing.getRequestHash().equals(requestHash)) {
          writeErrorResponse(
              response,
              HttpServletResponse.SC_CONFLICT,
              IDEMPOTENCY_ERROR,
              "Idempotency-Key reuse with different request body");
          return;
        }

        response.setStatus(existing.getHttpStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(existing.getResponseBody());
        response.flushBuffer();
        return;
      }

      if (IdempotencyState.PROCESSING.equals(existing.getState())) {
        writeErrorResponse(
            response,
            HttpServletResponse.SC_CONFLICT,
            IDEMPOTENCY_ERROR,
            "Request already in progress for this Idempotency-Key");
        return;
      }

    } else {
      // Create PROCESSING record
      IdempotentRequest newRequest = new IdempotentRequest();
      newRequest.setIdempotencyKey(key);
      newRequest.setRequestHash(requestHash);
      newRequest.setState(IdempotencyState.PROCESSING);
      newRequest.setEndpoint(request.getRequestURI());

      repository.save(newRequest);
    }

    // Execute real request
    filterChain.doFilter(wrappedRequest, wrappedResponse);

    // Capture response
    byte[] content = wrappedResponse.getContentAsByteArray();
    String responseBody = new String(content, StandardCharsets.UTF_8);

    // Update record result
    Optional<IdempotentRequest> existingAfter = repository.findByIdempotencyKey(key);

    if (existingAfter.isPresent()) {
      IdempotentRequest existing = existingAfter.get();

      existing.setState(IdempotencyState.COMPLETED);
      existing.setHttpStatus(wrappedResponse.getStatus());
      existing.setResponseBody(responseBody);

      repository.save(existing);
    }

    wrappedResponse.copyBodyToResponse();
  }

  private String normalizeJson(String body) {
    try {
      Object json = objectMapper.readValue(body, Object.class);
      return objectMapper.writeValueAsString(json);
    } catch (Exception e) {
      throw new RuntimeException("Error normalizing JSON", e);
    }
  }

  private String hashRequest(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

      StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
      for (byte b : hashBytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error generating hash", e);
    }
  }

  private void writeErrorResponse(
      HttpServletResponse response, int status, String errorCode, String message)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    Map<String, Object> errorDetails = new LinkedHashMap<>();
    errorDetails.put("timestamp", java.time.OffsetDateTime.now().toString());
    errorDetails.put("status", status);
    errorDetails.put("error", errorCode);
    errorDetails.put("message", message);

    String jsonResponse = objectMapper.writeValueAsString(errorDetails);

    response.getWriter().write(jsonResponse);
    response.flushBuffer();
  }
}
