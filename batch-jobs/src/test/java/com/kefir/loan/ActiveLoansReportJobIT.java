package com.kefir.loan;

import static org.assertj.core.api.Assertions.assertThat;

import com.kefir.entities.Account;
import com.kefir.entities.AccountType;
import com.kefir.entities.AmortizationType;
import com.kefir.entities.Bank;
import com.kefir.entities.Currency;
import com.kefir.entities.Customer;
import com.kefir.entities.CustomerType;
import com.kefir.entities.DocumentType;
import com.kefir.entities.Loan;
import com.kefir.entities.LoanType;
import com.kefir.entities.PersonType;
import com.kefir.entities.User;
import com.kefir.enums.AccountStatus;
import com.kefir.enums.CustomerStatus;
import com.kefir.enums.LoanStatus;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class ActiveLoansReportJobIT {

  @Container
  static PostgreSQLContainer postgres =
      new PostgreSQLContainer("postgres:16-alpine")
          .withDatabaseName("kefir_batch_test")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
  }

  @Autowired private JobOperator jobOperator;

  @Autowired
  @Qualifier("loanReportJob")
  private Job loanReportJob;

  @Autowired private EntityManager em;

  @Autowired private TransactionTemplate transactionTemplate;

  @Autowired private JdbcTemplate jdbcTemplate;

  @TempDir Path tempDir;

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute(
        "INSERT INTO core_user (id, username, password, fullname, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'SYSTEM', 'pass', 'SYSTEM', true, 1, NOW(), 1,"
            + " NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO document_type (id, name, enabled, created_by, created_at, updated_by,"
            + " updated_at) VALUES (1, 'DNI', true, 1, NOW(), 1, NOW()) ON CONFLICT (id) DO"
            + " NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO person_type (id, name, description, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'PHYSICAL', 'Physical Person', true, 1, NOW(),"
            + " 1, NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO customer_type (id, name, description, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'INDIVIDUAL', 'Individual', true, 1, NOW(), 1,"
            + " NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO bank (id, name, status, created_by, created_at, updated_by, updated_at) VALUES"
            + " (1, 'Kefir Bank', 'ACTIVE', 1, NOW(), 1, NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO account_type (id, name, code, description, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'SAVINGS_ACCOUNT', 1, 'Savings Account', true,"
            + " 1, NOW(), 1, NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO currency (id, iso_code, description, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'USD', 'US Dollar', true, 1, NOW(), 1, NOW()) ON"
            + " CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO amortization_type (id, name, description, enabled, created_by, created_at,"
            + " updated_by, updated_at) VALUES (1, 'FRENCH', 'French Amortization', true, 1, NOW(),"
            + " 1, NOW()) ON CONFLICT (id) DO NOTHING");
    jdbcTemplate.execute(
        "INSERT INTO loan_type (id, name, description, annual_interest_rate, enabled, created_by,"
            + " created_at, updated_by, updated_at) VALUES (1, 'PERSONAL', 'Personal Loan', 15.00,"
            + " true, 1, NOW(), 1, NOW()) ON CONFLICT (id) DO NOTHING");

    jdbcTemplate.execute("SELECT setval('core_user_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('document_type_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('person_type_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('customer_type_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('bank_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('account_type_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('currency_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('amortization_type_id_seq', 10, true)");
    jdbcTemplate.execute("SELECT setval('loan_type_id_seq', 10, true)");
  }

  @Test
  @DisplayName(
      "Should execute ActiveLoansReportJob against Testcontainers PostgreSQL and export CSV")
  void shouldRunActiveLoansReportJobAndProduceCsv() throws Exception {
    Long[] loanIds =
        transactionTemplate.execute(
            status -> {
              User systemUser = em.find(User.class, 1);
              Customer customer = createCustomer(systemUser);
              Account account = createAccount(customer, systemUser);

              // 1. Active overdue loan (expiration date: 2026-11-20, report date: 2026-11-27 -> 7
              // days past due)
              OffsetDateTime overdueDate =
                  OffsetDateTime.of(2026, 11, 20, 0, 0, 0, 0, ZoneOffset.UTC);
              Loan overdueLoan =
                  createLoan(
                      customer,
                      account,
                      LoanStatus.ACTIVE,
                      new BigDecimal("10000.00"),
                      new BigDecimal("7500.00"),
                      overdueDate,
                      systemUser);

              // 2. Active on-time loan (expiration date: 2026-12-25 -> 0 days past due)
              OffsetDateTime futureDate =
                  OffsetDateTime.of(2026, 12, 25, 0, 0, 0, 0, ZoneOffset.UTC);
              Loan currentLoan =
                  createLoan(
                      customer,
                      account,
                      LoanStatus.ACTIVE,
                      new BigDecimal("5000.00"),
                      new BigDecimal("5000.00"),
                      futureDate,
                      systemUser);

              // 3. Pending loan (should be filtered out by statusFilter=ACTIVE)
              Loan pendingLoan =
                  createLoan(
                      customer,
                      account,
                      LoanStatus.PENDING,
                      new BigDecimal("3000.00"),
                      new BigDecimal("3000.00"),
                      futureDate,
                      systemUser);

              return new Long[] {overdueLoan.getId(), currentLoan.getId(), pendingLoan.getId()};
            });

    assertThat(loanIds).isNotNull();
    Long overdueLoanId = loanIds[0];
    Long currentLoanId = loanIds[1];
    Long pendingLoanId = loanIds[2];

    File outputFile = tempDir.resolve("ACTIVE_LOANS_IT_REPORT.csv").toFile();

    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("reportDate", "2026-11-27")
            .addString("statusFilter", "ACTIVE")
            .addString("outputPath", outputFile.getAbsolutePath())
            .addLong("run.id", 1L)
            .toJobParameters();

    JobExecution execution = jobOperator.start(loanReportJob, jobParameters);

    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(outputFile).exists();

    List<String> lines = Files.readAllLines(outputFile.toPath());
    // 1 header + 2 active loans (pending loan excluded)
    assertThat(lines).hasSize(3);
    assertThat(lines.getFirst()).isEqualTo(ActiveLoansReportJobConfig.CSV_HEADER);

    String overdueLine =
        lines.stream().filter(l -> l.startsWith(overdueLoanId + ",")).findFirst().orElseThrow();
    String[] overdueFields = overdueLine.split(",");
    assertThat(overdueFields[0]).isEqualTo(String.valueOf(overdueLoanId));
    assertThat(overdueFields[2]).isEqualTo("ACTIVE");
    assertThat(new BigDecimal(overdueFields[3])).isEqualByComparingTo("10000.00");
    assertThat(new BigDecimal(overdueFields[4])).isEqualByComparingTo("7500.00");
    assertThat(overdueFields[5]).isEqualTo("2026-11-20");
    assertThat(overdueFields[6]).isEqualTo("7");

    String currentLine =
        lines.stream().filter(l -> l.startsWith(currentLoanId + ",")).findFirst().orElseThrow();
    String[] currentFields = currentLine.split(",");
    assertThat(currentFields[0]).isEqualTo(String.valueOf(currentLoanId));
    assertThat(currentFields[2]).isEqualTo("ACTIVE");
    assertThat(new BigDecimal(currentFields[3])).isEqualByComparingTo("5000.00");
    assertThat(new BigDecimal(currentFields[4])).isEqualByComparingTo("5000.00");
    assertThat(currentFields[5]).isEqualTo("2026-12-25");
    assertThat(currentFields[6]).isEqualTo("0");

    boolean pendingPresent = lines.stream().anyMatch(l -> l.startsWith(pendingLoanId + ","));
    assertThat(pendingPresent).isFalse();

    // Verify idempotency on rerun: running again overwrites file without duplicate rows
    JobParameters rerunParameters =
        new JobParametersBuilder()
            .addString("reportDate", "2026-11-27")
            .addString("statusFilter", "ACTIVE")
            .addString("outputPath", outputFile.getAbsolutePath())
            .addLong("run.id", 2L)
            .toJobParameters();

    JobExecution rerunExecution = jobOperator.start(loanReportJob, rerunParameters);
    assertThat(rerunExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    List<String> rerunLines = Files.readAllLines(outputFile.toPath());
    assertThat(rerunLines).hasSize(3);
    assertThat(rerunLines.getFirst()).isEqualTo(ActiveLoansReportJobConfig.CSV_HEADER);
  }

  private Customer createCustomer(User user) {
    DocumentType docType = em.find(DocumentType.class, 1);
    PersonType personType = em.find(PersonType.class, 1);
    CustomerType customerType = em.find(CustomerType.class, 1);

    String docNum = String.format("DOC%010d", System.currentTimeMillis() % 10000000000L);

    Customer customer =
        Customer.builder()
            .name1("Integration")
            .lastname1("TestCustomer")
            .fullname("Integration TestCustomer")
            .documentType(docType)
            .personType(personType)
            .customerType(customerType)
            .documentNumber(docNum)
            .email("it.customer@example.com")
            .status(CustomerStatus.ACTIVE)
            .createdBy(user)
            .createdAt(OffsetDateTime.now())
            .updatedBy(user)
            .updatedAt(OffsetDateTime.now())
            .build();
    em.persist(customer);
    return customer;
  }

  private Account createAccount(Customer customer, User user) {
    AccountType accountType = em.find(AccountType.class, 1);
    Currency currency = em.find(Currency.class, 1);
    Bank bank = em.find(Bank.class, 1);

    String accNum = String.format("100%010d", System.currentTimeMillis() % 10000000000L);
    String cbu = String.format("012345678901234567%04d", (int) (Math.random() * 9000) + 1000);

    Account account =
        new Account(
            0L,
            accountType,
            customer,
            currency,
            bank,
            accNum,
            cbu,
            new BigDecimal("50000.00"),
            AccountStatus.OPENED,
            user,
            OffsetDateTime.now(),
            user,
            OffsetDateTime.now());
    em.persist(account);
    return account;
  }

  private Loan createLoan(
      Customer customer,
      Account account,
      LoanStatus status,
      BigDecimal principal,
      BigDecimal remainingBalance,
      OffsetDateTime expirationDate,
      User user) {
    LoanType loanType = em.find(LoanType.class, 1);
    Currency currency = em.find(Currency.class, 1);
    AmortizationType amortizationType = em.find(AmortizationType.class, 1);
    OffsetDateTime now = OffsetDateTime.now();

    Loan loan =
        Loan.builder()
            .customer(customer)
            .account(account)
            .loanType(loanType)
            .numberOfInstallments(12)
            .currency(currency)
            .amortizationType(amortizationType)
            .annualInterestRate(new BigDecimal("15.00"))
            .monthlyInterestRate(new BigDecimal("1.25"))
            .principalAmount(principal)
            .interestAmount(new BigDecimal("1000.00"))
            .totalOperationAmount(principal.add(new BigDecimal("1000.00")))
            .openingDate(now.minusMonths(1))
            .expirationDate(expirationDate)
            .remainingBalance(remainingBalance)
            .accruedInterest(BigDecimal.ZERO)
            .status(status)
            .createdBy(user)
            .createdAt(now)
            .updatedBy(user)
            .updatedAt(now)
            .build();
    em.persist(loan);
    return loan;
  }
}
