package com.kefir.web.controllers;

import com.kefir.entities.Loan;
import com.kefir.orchestrators.LoanOrchestrator;
import com.kefir.services.LoanService;
import com.kefir.web.dtos.loan.LoanRequest;
import com.kefir.web.dtos.loan.LoanResponse;
import io.micrometer.core.annotation.Timed;
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

@Validated
@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loan services", description = "APIs for loans")
public class LoanController {

  private final LoanService loanService;
  private final LoanOrchestrator loanOrchestrator;

  public LoanController(LoanService loanService, LoanOrchestrator loanOrchestrator) {
    this.loanService = loanService;
    this.loanOrchestrator = loanOrchestrator;
  }

  @GetMapping
  public List<Loan> getAll() {
    return loanService.getAll();
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
  @Timed(
      value = "loan.get",
      percentiles = {0.5, 0.9, 0.95, 0.99},
      histogram = true)
  public ResponseEntity<LoanResponse> getById(
      @Parameter(
              name = "loanId",
              description = "Unique identifier of the loan to retrieve",
              required = true,
              example = "12345")
          @PathVariable
          Long loanId) {
    return ResponseEntity.ok(loanOrchestrator.getLoanData(loanId));
  }

  // Endpoint to create a new loan
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','OPR')")
  @Timed(
      value = "loan.create.request",
      description = "Time taken to create loan",
      percentiles = {0.5, 0.9, 0.95, 0.99},
      histogram = true)
  public ResponseEntity<LoanResponse> createLoan(@RequestBody @Valid LoanRequest loanRequest) {
    return ResponseEntity.ok(loanOrchestrator.createLoan(loanRequest));
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
}
