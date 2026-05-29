package com.kefir.web.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kefir.exceptions.*;
import com.kefir.web.dtos.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
          "uk_external_id", "External id already exists",
          "uk_customer_document", "Customer document already exists",
          "uk_account_cbu", "CBU already exists"
  );

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

    return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", fieldErrors);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
    return buildResponse(
        HttpStatus.UNAUTHORIZED, "Authentication error", "Authentication token expired");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    return buildResponse(HttpStatus.FORBIDDEN, "Forbidden action", "Authentication token expired");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
    log.error("Unhandled exception", ex);
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Unexpected error");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
          DataIntegrityViolationException ex) {

    String constraintName = null;

    Throwable cause = ex;

    while (cause != null) {
      if (cause instanceof ConstraintViolationException cve) {
        constraintName = cve.getConstraintName();
        break;
      }
      cause = cause.getCause();
    }

    String message = CONSTRAINT_MESSAGES.getOrDefault(
            constraintName,
            "Database integrity violation"
    );

    return buildResponse(
            HttpStatus.CONFLICT,
            "Conflict",
            message
    );
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {

    return buildResponse(ex.getStatus(), ex.getError(), ex.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidJson(
          HttpMessageNotReadableException ex) {

    Throwable cause = ex.getCause();

    if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {

        String fieldName = ife.getPath().getFirst().getFieldName();

        Object[] acceptedValues = ife.getTargetType().getEnumConstants();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid enum value",
                "Field '%s' accepts: %s"
                        .formatted(fieldName, java.util.Arrays.toString(acceptedValues))
        );
      }


    return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Malformed request",
            "Invalid request body"
    );
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
      HttpStatus status, String error, Object message) {

    ApiErrorResponse apiErrorResponse =
        new ApiErrorResponse(error, message, status.value(), OffsetDateTime.now());

    return ResponseEntity.status(status).body(apiErrorResponse);
  }


}
