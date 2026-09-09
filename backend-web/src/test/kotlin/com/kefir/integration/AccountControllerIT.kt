package com.kefir.integration

import com.jayway.jsonpath.JsonPath
import com.kefir.entities.Account
import com.kefir.entities.Customer
import com.kefir.enums.AccountStatus
import com.kefir.enums.AccountType
import com.kefir.enums.CurrencyIsoCodes
import com.kefir.enums.CustomerStatus
import com.kefir.enums.CustomerType
import com.kefir.enums.DocumentType
import com.kefir.enums.EntityName
import com.kefir.enums.LogOperation
import com.kefir.enums.PersonType
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthenticatedUser
import com.kefir.repositories.AccountRepository
import com.kefir.repositories.AccountTypeRepository
import com.kefir.repositories.BankBranchRepository
import com.kefir.repositories.BankRepository
import com.kefir.repositories.CurrencyRepository
import com.kefir.repositories.CustomerRepository
import com.kefir.repositories.CustomerTypeRepository
import com.kefir.repositories.DocumentTypeRepository
import com.kefir.repositories.OperationLogRepository
import com.kefir.repositories.PersonTypeRepository
import com.kefir.repositories.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.OffsetDateTime

@Transactional
class AccountControllerIT : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var accountTypeRepository: AccountTypeRepository

    @Autowired
    lateinit var bankRepository: BankRepository

    @Autowired
    lateinit var bankBranchRepository: BankBranchRepository

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var customerTypeRepository: CustomerTypeRepository

    @Autowired
    lateinit var documentTypeRepository: DocumentTypeRepository

    @Autowired
    lateinit var operationLogRepository: OperationLogRepository

    @Autowired
    lateinit var personTypeRepository: PersonTypeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var authToken: UsernamePasswordAuthenticationToken

    @BeforeEach
    fun setup() {
        val personType = personTypeRepository.findByNameIgnoreCase(PersonType.NATURAL.name).orElseThrow { ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND) }
        val documentType = documentTypeRepository.findByNameIgnoreCase(DocumentType.DNI.name).orElseThrow { ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND) }
        val customerType = customerTypeRepository.findByNameIgnoreCase(CustomerType.RETAIL.name).orElseThrow { ApiException(ErrorCode.CUSTOMER_NOT_FOUND) }
        val user = userRepository.findByUsername("admin").orElseThrow { ApiException(ErrorCode.USER_NOT_FOUND) }

        customerRepository.save(

            Customer.builder()
                .name1("John")
                .lastname1("Doe")
                .fullname("John Doe")
                .personType(personType)
                .documentType(documentType)
                .documentNumber("33111344")
                .customerType(customerType)
                .createdBy(user)
                .createdAt(OffsetDateTime.now())
                .updatedBy(user)
                .updatedAt(OffsetDateTime.now())
                .status(CustomerStatus.ACTIVE)
                .build(),

        )

        val principal = AuthenticatedUser(
            1,
            "admin",
        )

        authToken = UsernamePasswordAuthenticationToken(
            principal,
            null,
            listOf(SimpleGrantedAuthority("ROLE_ADMIN")),
        )

        SecurityContextHolder.getContext().authentication = authToken
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun createAccountSuccessfully() {
        val requestBody =
            javaClass.classLoader
                .getResource("requests/account/create-account-success.json")
                ?.readText()
                ?: throw IllegalStateException("File not found")

        val result = mockMvc.post("/api/accounts") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isCreated() }
            jsonPath("id") { exists() }
            jsonPath("status") { value("PENDING") }
        }.andReturn()

        val responseString = result.response.contentAsString
        val createdId = JsonPath.read<Number>(responseString, "$.id").toLong()

        // DB validation
        val account = accountRepository.findById(createdId).orElseThrow()
        assertThat(account).isNotNull

        val operationLog = operationLogRepository.findByEntityAndEntityIdAndOperation(EntityName.ACCOUNT.name, account.id, LogOperation.CREATION.name).orElseThrow()
        assertThat(operationLog.comments).isEqualTo("Account with id: ${account.id} created")
    }

    @Test
    fun createAccountFailWhenUserIsUnauthorized() {
        val unauthorizedUser = AuthenticatedUser(2, "regular_user")
        val lowPrivilegeToken = UsernamePasswordAuthenticationToken(
            unauthorizedUser,
            null,
            listOf(SimpleGrantedAuthority("ROLE_USER")),
        )
        SecurityContextHolder.getContext().authentication = lowPrivilegeToken

        val requestBody = javaClass.classLoader
            .getResource("requests/account/create-account-success.json")
            ?.readText() ?: throw IllegalStateException("File not found")

        mockMvc.post("/api/accounts") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun createAccountFailWhenPayloadIsInvalid() {
        val invalidRequestBody = "{}"

        mockMvc.post("/api/accounts") {
            contentType = MediaType.APPLICATION_JSON
            content = invalidRequestBody
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun getAllAccountsSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.PENDING)
        createTestAccount(AccountType.CHECKING_ACCOUNT, AccountStatus.OPENED)

        mockMvc.get("/api/accounts") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$") { isArray() }
            jsonPath("length()") { value(2) }
            // First account data
            jsonPath("[0]id") { exists() }
            jsonPath("[0]customer") { value("John Doe") }
            jsonPath("[0]type") { value("SAVINGS ACCOUNT") }
            jsonPath("[0]currencyIsoCode") { value("USD") }
            jsonPath("[0]bank") { value("KEFIR BANK") }
            jsonPath("[0]cbu") { exists() }
            jsonPath("[0]balance") { value(BigDecimal("10000.0")) }
            jsonPath("[0]status") { value("PENDING") }
            // Second account data
            jsonPath("[1]id") { exists() }
            jsonPath("[1]customer") { value("John Doe") }
            jsonPath("[1]type") { value("CHECKING ACCOUNT") }
            jsonPath("[1]currencyIsoCode") { value("USD") }
            jsonPath("[1]bank") { value("KEFIR BANK") }
            jsonPath("[1]cbu") { exists() }
            jsonPath("[1]balance") { value(BigDecimal("10000.0")) }
            jsonPath("[1]status") { value("OPENED") }
        }
    }

    @Test
    fun getAllAccountsFailWhenDatabaseIsEmpty() {
        mockMvc
            .get("/api/accounts") {
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                jsonPath("length()") { value(0) }
            }
    }

    @Test
    fun getAccountByIdSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.PENDING)

        mockMvc.get("/api/accounts/1") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("id") { exists() }
            jsonPath("customer") { value("John Doe") }
            jsonPath("type") { value("SAVINGS ACCOUNT") }
            jsonPath("currencyIsoCode") { value("USD") }
            jsonPath("bank") { value("KEFIR BANK") }
            jsonPath("cbu") { exists() }
            jsonPath("balance") { value(BigDecimal("10000.0")) }
            jsonPath("status") { value("PENDING") }
        }
    }

    @Test
    fun getAccountByIdFailWhenIdNotFound() {
        mockMvc
            .get("/api/accounts/1") { contentType = MediaType.APPLICATION_JSON }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun openAccountSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.PENDING)

        mockMvc
            .patch("/api/accounts/1/open") { contentType = MediaType.APPLICATION_JSON }.andExpect {
                status { isOk() }
                jsonPath("status") { value("OPENED") }
            }
    }

    @Test
    fun closeAccountSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.OPENED)

        mockMvc
            .patch("/api/accounts/1/close") { contentType = MediaType.APPLICATION_JSON }.andExpect {
                status { isOk() }
                jsonPath("status") { value("CLOSED") }
            }
    }

    @Test
    fun suspendAccountSuccessfully() {
        val account = createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.OPENED)

        val requestBody = javaClass.classLoader
            .getResource("requests/account/suspend-account-success.json")
            ?.readText() ?: throw IllegalStateException("File not found")

        mockMvc
            .patch("/api/accounts/1/suspend") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }.andExpect {
                status { isOk() }
                jsonPath("status") { value("SUSPENDED") }
            }

        val operationLog = operationLogRepository.findByEntityAndEntityIdAndOperation(EntityName.ACCOUNT.name, account.id, LogOperation.SUSPENSION.name).orElseThrow()
        assertThat(operationLog.comments).isEqualTo("Too many requests in short time")
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED"], mode = EnumSource.Mode.EXCLUDE)
    fun suspendAccountFailWhenAccountIsNotOpened(status: AccountStatus) {
        val account = createTestAccount(AccountType.SAVINGS_ACCOUNT, status)

        val requestBody = javaClass.classLoader
            .getResource("requests/account/suspend-account-success.json")
            ?.readText() ?: throw IllegalStateException("File not found")

        mockMvc
            .patch("/api/accounts/${account.id}/suspend") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }.andExpect {
                status { isUnprocessableEntity() }
                jsonPath("message") { value("Account is not in a valid state") }
            }
    }

    @Test
    @Throws(Exception::class)
    fun getAllAccountsWithPagination() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.OPENED)
        createTestAccount(AccountType.CHECKING_ACCOUNT, AccountStatus.OPENED)
        createTestAccount(AccountType.SAVINGS_ACCOUNT, AccountStatus.OPENED)
        createTestAccount(AccountType.CHECKING_ACCOUNT, AccountStatus.OPENED)

        mockMvc
            .get("/api/accounts?page=2&size=2") { contentType = MediaType.APPLICATION_JSON }.andExpect {
                status { isOk() }
                jsonPath("length()") { value(2) }
                jsonPath("[0]id") { value(3) } // First customer data
                jsonPath("[1]id") { value(4) } // Second customer data
            }
    }

    @ParameterizedTest
    @CsvSource(
        "'page=0&size=2', 'Page must be greater than 0'",
        "'page=1&size=-3', 'Size must be greater than 0'",
        "'page=1&size=23', 'Size must not exceed 20'",
        "'page=2', 'Page and size must be provided together'",
        "'size=23', 'Page and size must be provided together'",
    )
    @Throws(Exception::class)
    fun getAllCustomersWithInvalidPaginationParameters_returnsBadRequest(
        queryParams: String?,
        expectedMessage: String?,
    ) {
        mockMvc
            .get("/api/accounts?$queryParams") { contentType = MediaType.APPLICATION_JSON }.andExpect {
                status { isBadRequest() }
                jsonPath("message") { value(expectedMessage) }
            }
    }

    private fun createTestAccount(accountType: AccountType, status: AccountStatus): Account {
        val accountType = accountTypeRepository.findByNameIgnoreCase(accountType.dbName).orElseThrow { ApiException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND) }

        val bankBranch = bankBranchRepository.findByBranchNumberAndBankId(1, 1).orElseThrow { ApiException(ErrorCode.BANK_BRANCH_NOT_FOUND) }

        val currency = currencyRepository.findByIsoCode(CurrencyIsoCodes.USD.name).orElseThrow { ApiException(ErrorCode.CURRENCY_NOT_FOUND) }

        val customer = customerRepository.findById(1).orElseThrow { ApiException(ErrorCode.CUSTOMER_NOT_FOUND) }

        val user = userRepository.findByUsername("operator").orElseThrow { ApiException(ErrorCode.USER_NOT_FOUND) }

        val randomCbu = "001000" + (1000000000000000..9999999999999999).random().toString()

        return accountRepository.saveAndFlush(
            Account(
                type = accountType,
                customer = customer,
                currency = currency,
                bank = bankBranch.bank,
                balance = BigDecimal("10000.00"),
                createdBy = user,
                updatedBy = user,
                accountNumber = "11111",
                cbu = randomCbu,
                status = status,
            ),
        )
    }
}
