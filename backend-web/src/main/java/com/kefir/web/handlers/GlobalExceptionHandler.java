package com.kefir.web.handlers;

import com.kefir.exceptions.CustomerCreationException;
import com.kefir.exceptions.CustomerNotFoundException;
import com.kefir.exceptions.CustomerNotValidException;
import com.kefir.exceptions.LoanNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({LoanNotFoundException.class, CustomerNotFoundException.class})
  public ResponseEntity<Map<String, Object>> handleAllNotFound(RuntimeException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
  }

  @ExceptionHandler(CustomerNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleCustomerInvalid(CustomerNotValidException ex) {
    return buildResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid Customer", ex.getMessage());
  }

  @ExceptionHandler(CustomerCreationException.class)
  public ResponseEntity<Map<String, Object>> handleCustomerCreation(CustomerCreationException ex) {
    return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Creation Exception", ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> fieldErrors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

    return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", fieldErrors);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
    return buildResponse(
        HttpStatus.FORBIDDEN, "You don't have permission to perform this action", ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
  }

  private ResponseEntity<Map<String, Object>> buildResponse(
      HttpStatus status, String error, Object message) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", error);
    body.put("message", message);
    return new ResponseEntity<>(body, status);
  }
}
