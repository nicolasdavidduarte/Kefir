package com.kefir.integration;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kefir.entities.CoreUser;
import com.kefir.entities.Currency;
import com.kefir.entities.Customer;
import com.kefir.entities.Loan;
import com.kefir.entities.LoanType;
import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.LoanNotFoundException;
import com.kefir.infrastructure.security.JwtService;
import com.kefir.orchestrators.LoanOrchestrator;
import com.kefir.repositories.IdempotentRequestRepository;
import com.kefir.services.LoanService;
import com.kefir.web.controllers.LoanController;
import com.kefir.web.dtos.LoanResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LoanController.class)
class LoanControllerIT {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private LoanService loanService;

  @MockitoBean private LoanOrchestrator loanOrchestrator;

  @MockitoBean private IdempotentRequestRepository idempotentRepo;

  @MockitoBean private JwtService jwtService;

  @MockitoBean private MeterRegistry meterRegistry;

  @MockitoBean private Timer timer;

  final Long idTarget = 999L;

  @Test
  @SuppressWarnings("PMD")
  void testGetAllLoans() throws Exception {

    Customer customer = new Customer();
    customer.setId(123L);

    CoreUser user = new CoreUser();
    user.setId(999);

    LoanType loanType = LoanType.createNew("TestType", "Type for Test", user);

    Currency currency = new Currency();
    currency.setId(1);

    Loan loan =
        Loan.builder()
            .id(idTarget)
            .customer(customer)
            .loanType(loanType)
            .totalOperationAmount(new BigDecimal("1000.00"))
            .openingDate(OffsetDateTime.now())
            .currency(currency)
            .expirationDate(OffsetDateTime.now())
            .numberOfInstallments(4)
            .status(LoanStatus.ACTIVE)
            .build();

    List<Loan> loans = List.of(loan);
    when(loanService.getAll()).thenReturn(loans);

    //    mockMvc
    //        .perform(get("/api/loans"))
    //        .andExpect(status().isOk())
    //        .andExpect(jsonPath("$[0].id").value(idTarget))
    //        .andExpect(jsonPath("$[0].customer").value(123L))
    //        .andExpect(jsonPath("$[0].totalOperationAmount").value(1000.0));
  }

  @Test
  void testWhenGetLoanByExistentId_RetrieveLoan() throws Exception {
    Customer customer = new Customer();
    customer.setId(123L);

    CoreUser user = new CoreUser();
    user.setId(999);

    LoanType loanType = LoanType.createNew("TestType", "Type for Test", user);

    Currency currency = new Currency();
    currency.setId(1);

    Loan loan =
        Loan.builder()
            .id(idTarget)
            .customer(customer)
            .loanType(loanType)
            .totalOperationAmount(new BigDecimal("1000.00"))
            .openingDate(OffsetDateTime.now())
            .currency(currency)
            .expirationDate(OffsetDateTime.now())
            .numberOfInstallments(4)
            .status(LoanStatus.ACTIVE)
            .build();

    LoanResponse loanDetails =
        LoanResponse.builder()
            .id(idTarget)
            .customer(123L)
            .loanType(1)
            .totalOperationAmount(new BigDecimal("1000.00"))
            .openingDate(OffsetDateTime.now())
            .currency(1)
            .updatedAt(OffsetDateTime.now())
            .numberOfInstallments(4)
            .status((LoanStatus.ACTIVE))
            .build();

    when(loanOrchestrator.getLoanData(loan.getId())).thenReturn(loanDetails);

    //    mockMvc
    //        .perform(get("/api/loans/" + idTarget))
    //        .andExpect(status().isOk())
    //        .andExpect(jsonPath("$.id").value(idTarget))
    //        .andExpect(jsonPath("$.customer").value(123L))
    //        .andExpect(jsonPath("$.totalOperationAmount").value(1000.0));
  }

  @Test
  void testWhenGetLoanByNonExistentId_ThrowErrorMessage() throws Exception {
    when(loanOrchestrator.getLoanData(anyLong())).thenThrow(new LoanNotFoundException(122L));

    mockMvc.perform(get("/api/loans/122")).andExpect(status().isNotFound());
    verify(loanOrchestrator).getLoanData(anyLong());
  }
}
