package com.kefir.integrationtests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kefir.entities.Loan;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.security.JwtService;
import com.kefir.orchestrators.LoanOrchestrator;
import com.kefir.repositories.IdempotentRequestRepository;
import com.kefir.services.LoanService;
import com.kefir.web.controllers.LoanController;
import com.kefir.web.dtos.LoanResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LoanController.class)
class LoanControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private LoanService loanService;

  @MockitoBean private LoanOrchestrator loanOrchestrator;

  @MockitoBean private IdempotentRequestRepository idempotentRepo;

  @MockitoBean private JwtService jwtService;

  @MockitoBean private MeterRegistry meterRegistry;

  @MockitoBean private Timer timer;

  @Test
  @SuppressWarnings("PMD")
  void testGetAllLoans() throws Exception {

    Loan loan =
        Loan.builder()
            .id(1L)
            .customer(123L)
            .loanType(1)
            .totalOperationAmount(1000.0)
            .openingDate(LocalDate.now())
            .currency(1)
            .expirationDate(LocalDate.now())
            .totalTermDays(90)
            .status(1)
            .build();

    List<Loan> loans = List.of(loan);
    when(loanService.findAll()).thenReturn(loans);

    mockMvc
        .perform(get("/api/loans"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].customer").value(123))
        .andExpect(jsonPath("$[0].totalOperationAmount").value(1000.0));
  }

  @Test
  void testWhenGetLoanByExistentId_RetrieveLoan() throws Exception {
    Loan loan =
        new Loan(
            1L,
            123L,
            1,
            1000.0,
            LocalDate.now(),
            1,
            LocalDate.now(),
            90,
            null,
            null,
            null,
            1,
            LocalDate.now(),
            1,
            null);

    LoanResponse loanDetails =
        LoanResponse.builder()
            .id(1L)
            .customer(123L)
            .loanType(1)
            .totalOperationAmount(1000.0)
            .openingDate(LocalDate.now())
            .currency(1)
            .lastModificationDate(LocalDate.now())
            .totalTermDays(90)
            .status(1)
            .build();

    when(loanOrchestrator.getLoanData(loan.getId())).thenReturn(loanDetails);

    mockMvc
        .perform(get("/api/loans/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.customer").value(123))
        .andExpect(jsonPath("$.totalOperationAmount").value(1000.0));
  }

  @Test
  void testWhenGetLoanByNonExistentId_ThrowErrorMessage() throws Exception {
    when(loanOrchestrator.getLoanData(anyLong())).thenThrow(new LoanNotFoundException(122L));

    mockMvc.perform(get("/api/loans/122")).andExpect(status().isNotFound());
    verify(loanOrchestrator).getLoanData(anyLong());
  }
}
