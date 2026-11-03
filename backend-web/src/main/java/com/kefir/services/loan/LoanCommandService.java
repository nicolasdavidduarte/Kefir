package com.kefir.services.loan;

import com.kefir.entities.*;
import com.kefir.enums.EntityName;
import com.kefir.enums.LogOperation;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.LoanRepository;
import com.kefir.services.account.AccountService;
import com.kefir.services.amortizationtype.AmortizationTypeService;
import com.kefir.services.currency.CurrencyService;
import com.kefir.services.customer.CustomerService;
import com.kefir.services.loanInstallment.LoanInstallmentService;
import com.kefir.services.loantype.LoanTypeService;
import com.kefir.services.operationlog.OperationLogService;
import com.kefir.services.user.UserService;
import com.kefir.web.dtos.loan.LoanChargeOffRequest;
import com.kefir.web.dtos.loan.LoanRequest;
import com.kefir.web.dtos.loan.LoanResponse;
import com.kefir.web.dtos.operationLog.OperationLogCommand;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoanCommandService {

  private final LoanRepository loanRepository;
  private final LoanInstallmentService loanInstallmentService;
  private final AccountService accountService;
  private final AuthService authService;
  private final UserService userService;
  private final MeterRegistry registry;
  private final CustomerService customerService;
  private final LoanTypeService loanTypeService;
  private final CurrencyService currencyService;
  private final AmortizationTypeService amortizationTypeService;
  private final OperationLogService operationLogService;

  public LoanCommandService(
      LoanRepository loanRepository,
      LoanInstallmentService loanInstallmentService,
      AccountService accountService,
      AuthService authService,
      MeterRegistry registry,
      CustomerService customerService,
      LoanTypeService loanTypeService,
      CurrencyService currencyService,
      UserService userService,
      AmortizationTypeService amortizationTypeService,
      OperationLogService operationLogService) {
    this.loanRepository = loanRepository;
    this.registry = registry;
    this.authService = authService;
    this.loanInstallmentService = loanInstallmentService;
    this.customerService = customerService;
    this.loanTypeService = loanTypeService;
    this.currencyService = currencyService;
    this.userService = userService;
    this.amortizationTypeService = amortizationTypeService;
    this.accountService = accountService;
    this.operationLogService = operationLogService;
  }

  @Transactional
  public LoanResponse create(LoanRequest loanRequest) {

    boolean success = false;

    try {

      Customer customer = customerService.getById(loanRequest.customerId());
      customer.validateCanCreateLoan();

      Account account =
          accountService.getByIdAndCustomerId(loanRequest.accountId(), loanRequest.customerId());
      account.validateCanBeUsedToCreateLoan();

      OffsetDateTime now = OffsetDateTime.now();

      User user = userService.getById(authService.getCurrentUserId());

      LoanType loanType = loanTypeService.getByNameIgnoringCase(loanRequest.loanType());

      AmortizationType amortizationType =
          amortizationTypeService.getByNameIgnoringCase(loanRequest.amortizationType());

      Currency currency = currencyService.getByIsoCode(loanRequest.currencyIsoCode());

      Loan loan =
          Loan.create(
              customer,
              account,
              loanType,
              amortizationType,
              loanRequest.principalAmount(),
              currency,
              loanRequest.numberOfInstallments(),
              loanRequest.externalId(),
              user,
              now);

      loanRepository.save(loan);

      List<LoanInstallment> loanInstallments =
          loanInstallmentService.createInstallmentsSchedule(loan);

      loan.initializeFinancialAmounts(loanInstallments);

      operationLogService.log(
          new OperationLogCommand(
              LogOperation.CREATION,
              EntityName.LOAN,
              loan.getId(),
              "Loan successfully created",
              user));

      log.info("Loan successfully created - id: {}", loan);

      success = true;

      return LoanResponse.fromEntity(loan);

    } finally {
      registry.counter("loan.created", "status", success ? "success" : "error").increment();
    }
  }

  @Transactional
  public LoanResponse chargeOff(Long id, LoanChargeOffRequest chargeOffRequest) {
    Loan loan =
        loanRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));

    User user = userService.getById(authService.getCurrentUserId());

    loan.chargeOff(user, OffsetDateTime.now());

    loanInstallmentService.updateInstallmentsForChargeOff(id, user);

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.CHARGE_OFF,
            EntityName.LOAN,
            loan.getId(),
            chargeOffRequest.reason(),
            user));

    return LoanResponse.fromEntity(loan);
  }

  @Transactional
  public LoanResponse approve(Long id) {
    Loan loan =
        loanRepository
            .findByIdForUpdate(id)
            .orElseThrow(() -> new ApiException(ErrorCode.LOAN_NOT_FOUND));

    User user = userService.getById(authService.getCurrentUserId());

    loan.approve(user, OffsetDateTime.now());

    accountService.addBalance(loan.getAccount().getId(), loan.getPrincipalAmount());

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.APPROVAL,
            EntityName.LOAN,
            loan.getId(),
            "Loan successfully approved",
            user));

    return LoanResponse.fromEntity(loan);
  }
}
