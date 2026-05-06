package com.kefir.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kefir.entities.Loan;
import com.kefir.orchestrators.LoanOrchestrator;
import com.kefir.repositories.IdempotentRequestRepository;
import com.kefir.services.LoanService;
import com.kefir.web.DTOs.LoanRequest;
import com.kefir.web.DTOs.LoanResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Loan services", description = "APIs for loans")
@RestController
@Validated
public class LoanController {

  private final LoanService loanService;
  private final LoanOrchestrator loanOrchestrator;
  private final IdempotentRequestRepository idempotentRepo;
  private final ObjectMapper objectMapper;
  private final MeterRegistry meterRegistry;
  private final Timer timer;

  public LoanController(
      LoanService loanService,
      LoanOrchestrator loanOrchestrator,
      IdempotentRequestRepository idempotentRequestRepository,
      ObjectMapper objectMapper,
      MeterRegistry meterRegistry,
      Timer timer) {
    this.loanService = loanService;
    this.loanOrchestrator = loanOrchestrator;
    this.idempotentRepo = idempotentRequestRepository;
    this.objectMapper = objectMapper;
    this.meterRegistry = meterRegistry;
    this.timer = timer;
  }

  @GetMapping
  public List<Loan> getAll() {
    return loanService.findAll();
  }

  // Endpoint to retrieve a single record by ID using a JSON request body
  @Observed(name = "loan.controller.get")
  @GetMapping("/{loanId}")
  @Operation(
      summary = "Get loan details by id",
      description = "Returns loan data by its identification number")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Loan details returned successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"id\":12345, \"amount\":10000.00}"))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "404", description = "Loan not found"),
        @ApiResponse(responseCode = "409", description = "Invalid loan"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  // @Timed(value = "loan.get", percentiles = {0.5, 0.9, 0.95, 0.99}, histogram = true)
  public ResponseEntity<LoanResponse> getById(
      @Parameter(
              name = "loanId",
              description = "Unique identifier of the loan to retrieve",
              required = true,
              example = "12345")
          @PathVariable
          Long loanId) {

    //    Timer.Sample sample = Timer.start(meterRegistry);
    //
    //    try {
    //      LoanDataDTO data = loanDataOrchestrator.getLoanData(loanId);
    //
    //      sample.stop(
    //              Timer.builder("loan.get")
    //                      .tag("status", "success")
    //                      .register(meterRegistry)
    //      );
    //
    //      return ResponseEntity.ok(data);
    //
    //    } catch (Exception e) {
    //
    //      sample.stop(
    //              Timer.builder("loan.get")
    //                      .tag("status", "error")
    //                      .register(meterRegistry)
    //      );
    //
    //      throw e;
    //    }

    //    Timer.builder("loan.get")
    //            .register(meterRegistry)
    //            .record(() -> return ResponseEntity.ok(loanDataOrchestrator.getLoanData(loanId)));

    return timer.record(() -> ResponseEntity.ok(loanOrchestrator.getLoanData(loanId)));
  }

  // Endpoint to create a new loan
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @Timed(
      value = "loan.create.request",
      description = "Time taken to create loan",
      percentiles = {0.5, 0.9, 0.95, 0.99},
      histogram = true)
  public ResponseEntity<Loan> createLoan(@RequestBody @Valid LoanRequest loan) {
    return ResponseEntity.ok(loanOrchestrator.createLoan(loan));
  }

  // Endpoint to delete a loan
  @DeleteMapping("/{loanId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> deleteLoan(@PathVariable Long loanId) {
    loanService.deleteLoan(loanId);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Loan successfully deleted!");
    response.put("loanId", loanId);
    response.put("timestamp", LocalDateTime.now());

    return ResponseEntity.ok(response);
  }

  // Endpoint to update a loan
  @PutMapping("/{loanId}")
  public ResponseEntity<Map<String, Object>> updateLoan(
      @Parameter(
              name = "loanId",
              description = "Unique identifier of the loan to update",
              required = true,
              example = "12345")
          @PathVariable
          Long loanId,
      @RequestBody LoanRequest loan) {
    loanService.updateLoan(loanId, loan);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Loan successfully updated!");
    response.put("loanId", loan.getId());
    response.put("timestamp", LocalDateTime.now());

    return ResponseEntity.ok(response);
  }
}
