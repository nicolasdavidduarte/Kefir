package org.kefir.controllers;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kefir.DTOs.LoanDTO;
import org.kefir.DTOs.LoanDataDTO;
import org.kefir.entities.Loan;
import org.kefir.orchestrators.LoanDataOrchestrator;
import org.kefir.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/loan")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Loan services", description = "APIs for loans")
@RestController
@Validated
@RequiredArgsConstructor
public class LoanController {

  private final LoanService loanService;
  private final LoanDataOrchestrator loanDataOrchestrator;

  // Endpoint to retrieve all records from the person_type table
  @GetMapping
  public List<Loan> getAll() {
    return loanService.findAll();
  }

  // Endpoint to retrieve a single record by ID using a JSON request body
  @GetMapping("/getLoanById/{loanId}")
  @Operation(
      summary = "Get loan details by id",
      description = "Returns loan data by its identification number")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Loan details returned successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
        @ApiResponse(responseCode = "404", description = "Loan not found"),
        @ApiResponse(responseCode = "409", description = "Invalid loan"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @Timed
  @Counted
  public ResponseEntity<LoanDataDTO> getById(
      @Parameter(
              name = "loanId",
              description = "Unique identifier of the loan to retrieve",
              required = true,


                      example = "12345")
          @PathVariable
          Long loanId) {
    Optional<LoanDataDTO> loanDetails =
        Optional.ofNullable(loanDataOrchestrator.getLoanData(loanId));

    return loanDetails
        .map(details -> new ResponseEntity<>(details, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  // Endpoint to create a new loan
  @PostMapping("/createLoan")
  @Counted
  @Timed(value = "loan.create.request", description = "Time taken to create loan")
  public ResponseEntity<Loan> createLoan(@RequestBody LoanDTO loan) {
    Loan createdLoan = loanService.create(loan);
    return ResponseEntity.ok(createdLoan);
  }

  // Endpoint to delete a loan
  @PostMapping("/deleteLoan")
  public ResponseEntity<String> deleteLoan(@RequestBody Map<String, Long> request) {
    Long id = request.get("id");
    loanService.deleteLoan(id);
    return ResponseEntity.ok("Loan successfully deleted!");
  }

  // Endpoint to update a loan
  @PostMapping("/updateLoan")
  public ResponseEntity<String> updateLoan(@RequestBody LoanDTO loanDTO) {
    loanService.updateLoan(loanDTO);
    return ResponseEntity.ok("Loan successfully updated!");
  }
}
