package com.kefir.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kefir.entities.*;
import com.kefir.entities.AccountType;
import com.kefir.entities.CustomerType;
import com.kefir.entities.DocumentType;
import com.kefir.entities.PersonType;
import com.kefir.enums.*;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthenticatedUser;
import com.kefir.repositories.AccountRepository;
import com.kefir.repositories.AccountTypeRepository;
import com.kefir.repositories.AmortizationTypeRepository;
import com.kefir.repositories.BankRepository;
import com.kefir.repositories.CurrencyRepository;
import com.kefir.repositories.CustomerRepository;
import com.kefir.repositories.CustomerTypeRepository;
import com.kefir.repositories.DocumentTypeRepository;
import com.kefir.repositories.LoanRepository;
import com.kefir.repositories.LoanTypeRepository;
import com.kefir.repositories.PersonTypeRepository;
import com.kefir.repositories.UserRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

class LoanControllerIT extends IntegrationTestBase {
  @Autowired private MockMvc mockMvc;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private PersonTypeRepository personTypeRepository;

  @Autowired private DocumentTypeRepository documentTypeRepository;

  @Autowired private CustomerTypeRepository customerTypeRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private LoanTypeRepository loanTypeRepository;

  @Autowired private CurrencyRepository currencyRepository;

  @Autowired private AmortizationTypeRepository amortizationTypeRepository;

  @Autowired private LoanRepository loanRepository;

  @Autowired private AccountRepository accountRepository;

  @Autowired private AccountTypeRepository accountTypeRepository;

  @Autowired private BankRepository bankRepository;

  @BeforeEach
  void setup() {

    AuthenticatedUser principal = new AuthenticatedUser(1, "admin");

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
            principal, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void createLoanSuccessfully() throws Exception {

    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.ACTIVE);

    createTestAccount(customer, AccountStatus.OPENED);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/loan/create-loan-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  void createLoanFailWhenAccountIsNotOpened() throws Exception {

    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.ACTIVE);

    createTestAccount(customer, AccountStatus.PENDING);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/loan/create-loan-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  void createLoanFailWhenPayloadIsInvalid() throws Exception {

    String invalidRequestBody = "{}";

    mockMvc
        .perform(
            post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(invalidRequestBody))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void createLoanFailWhenUserIsUnauthorized() throws Exception {
    AuthenticatedUser unauthorizedUser = new AuthenticatedUser(2, "viewer_user");
    UsernamePasswordAuthenticationToken lowPrivilegeToken =
        new UsernamePasswordAuthenticationToken(
            unauthorizedUser, null, List.of(new SimpleGrantedAuthority("ROLE_VIEWER")));

    SecurityContextHolder.getContext().setAuthentication(lowPrivilegeToken);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/loan/create-loan-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void createLoanFailWhenUserIsOperator() throws Exception {
    AuthenticatedUser operatorUser = new AuthenticatedUser(2, "operator_user");
    UsernamePasswordAuthenticationToken lowPrivilegeToken =
        new UsernamePasswordAuthenticationToken(
            operatorUser, null, List.of(new SimpleGrantedAuthority("ROLE_OPR")));

    SecurityContextHolder.getContext().setAuthentication(lowPrivilegeToken);

    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.ACTIVE);
    createTestAccount(customer, AccountStatus.OPENED);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/loan/create-loan-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  void createLoanFailWhenDuplicated() throws Exception {

    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.ACTIVE);
    createTestAccount(customer, AccountStatus.OPENED);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/loan/create-loan-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isCreated());

    mockMvc
        .perform(post("/api/loans").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isConflict());
  }

  @Test
  void getAllLoansSuccessfully() throws Exception {
    Customer customer1 = createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);
    createTestAccount(customer1, AccountStatus.OPENED);
    createTestLoan(customer1, 998L);

    Customer customer2 = createTestCustomer(2, "123456788", CustomerStatus.ACTIVE);
    createTestAccount(customer2, AccountStatus.OPENED);
    createTestLoan(customer2, 999L);

    mockMvc
        .perform(get("/api/loans").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        // First loan data
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].externalId").exists())
        .andExpect(jsonPath("$[0].customer").value("John Doe"))
        .andExpect(jsonPath("$[0].loanType").value("PERSONAL"))
        .andExpect(jsonPath("$[0].amortizationType").value("FRENCH"))
        .andExpect(jsonPath("$[0].currency").value("USD"))
        .andExpect(jsonPath("$[0].numberOfInstallments").value(4))
        .andExpect(jsonPath("$[0].annualInterestRate").value("75.0"))
        .andExpect(jsonPath("$[0].monthlyInterestRate").value("6.25"))
        .andExpect(jsonPath("$[0].totalPrincipal").value("10000.0"))
        .andExpect(jsonPath("$[0].totalInterest").value("2500.0"))
        .andExpect(jsonPath("$[0].openingDate").exists())
        .andExpect(jsonPath("$[0].expirationDate").exists())
        .andExpect(jsonPath("$[0].status").value("ACTIVE"))
        .andExpect(jsonPath("$[0].createdAt").exists())
        .andExpect(jsonPath("$[0].user").value("admin"))
        //                // Second customer data
        .andExpect(jsonPath("$[1].id").exists())
        .andExpect(jsonPath("$[1].externalId").exists())
        .andExpect(jsonPath("$[1].customer").value("John Doe"))
        .andExpect(jsonPath("$[1].loanType").value("PERSONAL"))
        .andExpect(jsonPath("$[1].amortizationType").value("FRENCH"))
        .andExpect(jsonPath("$[1].currency").value("USD"))
        .andExpect(jsonPath("$[1].numberOfInstallments").value(4))
        .andExpect(jsonPath("$[1].annualInterestRate").value("75.0"))
        .andExpect(jsonPath("$[1].monthlyInterestRate").value("6.25"))
        .andExpect(jsonPath("$[1].totalPrincipal").value("10000.0"))
        .andExpect(jsonPath("$[1].totalInterest").value("2500.0"))
        .andExpect(jsonPath("$[1].openingDate").exists())
        .andExpect(jsonPath("$[1].expirationDate").exists())
        .andExpect(jsonPath("$[1].status").value("ACTIVE"))
        .andExpect(jsonPath("$[1].createdAt").exists())
        .andExpect(jsonPath("$[1].user").value("admin"));
  }

  @Test
  void getAllLoansReturnsEmptyListWhenDatabaseIsEmpty() throws Exception {

    mockMvc.perform(get("/api/loans"));

    mockMvc
        .perform(get("/api/loans").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getLoanByIdSuccessfully() throws Exception {
    Customer customer1 = createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);
    createTestLoan(customer1, 999L);

    mockMvc
        .perform(get("/api/loans/1").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("externalId").exists())
        .andExpect(jsonPath("customer").value("John Doe"))
        .andExpect(jsonPath("loanType").value("PERSONAL"))
        .andExpect(jsonPath("amortizationType").value("FRENCH"))
        .andExpect(jsonPath("currency").value("USD"))
        .andExpect(jsonPath("numberOfInstallments").value(4))
        .andExpect(jsonPath("annualInterestRate").value("75.0"))
        .andExpect(jsonPath("monthlyInterestRate").value("6.25"))
        .andExpect(jsonPath("totalPrincipal").value("10000.0"))
        .andExpect(jsonPath("totalInterest").value("2500.0"))
        .andExpect(jsonPath("openingDate").exists())
        .andExpect(jsonPath("expirationDate").exists())
        .andExpect(jsonPath("status").value("ACTIVE"))
        .andExpect(jsonPath("createdAt").exists())
        .andExpect(jsonPath("user").value("admin"));
  }

  @Test
  void getLoanByIdFailWhenNotFound() throws Exception {
    mockMvc
        .perform(get("/api/loans/1").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  private Customer createTestCustomer(Integer id, String documentNumber, CustomerStatus status) {
    PersonType personType =
        personTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND));
    DocumentType documentType =
        documentTypeRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND));
    CustomerType customerType =
        customerTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));
    User user =
        userRepository.findById(1).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    return customerRepository.save(
        Customer.builder()
            .name1("John")
            .lastname1("Doe")
            .fullname("John Doe")
            .personType(personType)
            .documentType(documentType)
            .documentNumber(documentNumber)
            .customerType(customerType)
            .user(user)
            .status(status)
            .build());
  }

  private Account createTestAccount(Customer customer, AccountStatus status) {

    String name = com.kefir.enums.AccountType.SAVINGS_ACCOUNT.getDbName();
    AccountType accountType =
        accountTypeRepository
            .findByNameIgnoreCase(name)
            .orElseThrow(() -> new ApiException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND));

    Currency currency =
        currencyRepository
            .findByIsoCode(CurrencyIsoCodes.USD.name())
            .orElseThrow(() -> new ApiException(ErrorCode.CURRENCY_NOT_FOUND));

    Bank bank =
        bankRepository.findById(1).orElseThrow(() -> new ApiException(ErrorCode.BANK_NOT_FOUND));

    BigDecimal initialBalance = new BigDecimal("10000.00");

    User user =
        userRepository.findById(1).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    String cbu = UUID.randomUUID().toString().substring(1, 22);

    Account account =
        new Account(
            0L,
            accountType,
            customer,
            currency,
            bank,
            cbu,
            initialBalance,
            status,
            user,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    return accountRepository.save(account);
  }

  private void createTestLoan(Customer customer, Long externalId) {

    Account account = createTestAccount(customer, AccountStatus.OPENED);

    LoanType loanType =
        loanTypeRepository
            .findByNameIgnoringCase(LoanTypeName.PERSONAL.name())
            .orElseThrow(() -> new ApiException(ErrorCode.LOAN_TYPE_NOT_FOUND));

    Currency currency =
        currencyRepository
            .findByIsoCode(CurrencyIsoCodes.USD.name())
            .orElseThrow(() -> new ApiException(ErrorCode.CURRENCY_NOT_FOUND));

    AmortizationType amortizationType =
        amortizationTypeRepository
            .findByName(AmortizationTypeName.FRENCH)
            .orElseThrow(() -> new ApiException(ErrorCode.AMORTIZATION_TYPE_NOT_FOUND));

    OffsetDateTime now = OffsetDateTime.now();

    User user =
        userRepository.findById(1).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    loanRepository.save(
        Loan.builder()
            .customer(customer)
            .account(account)
            .loanType(loanType)
            .numberOfInstallments(4)
            .currency(currency)
            .amortizationType(amortizationType)
            .annualInterestRate(new BigDecimal("75.00"))
            .monthlyInterestRate(new BigDecimal("6.25"))
            .principalAmount(new BigDecimal("10000.00"))
            .interestAmount(new BigDecimal("2500.00"))
            .totalOperationAmount(new BigDecimal("12500.00"))
            .openingDate(now)
            .expirationDate(now.plusMonths(4))
            .externalId(externalId)
            .status(LoanStatus.ACTIVE)
            .user(user)
            .createdAt(now)
            .updatedAt(now)
            .build());
  }
}
