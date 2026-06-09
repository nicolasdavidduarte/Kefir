package com.kefir.integration

import com.kefir.entities.Account
import com.kefir.entities.Customer
import com.kefir.enums.AccountType
import com.kefir.enums.CurrencyIsoCodes
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
import com.kefir.repositories.PersonTypeRepository
import com.kefir.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class AccountControllerIT : IntegrationTestBase() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var bankRepository: BankRepository

    @Autowired
    lateinit var bankBranchRepository: BankBranchRepository

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var personTypeRepository: PersonTypeRepository

    @Autowired
    lateinit var documentTypeRepository: DocumentTypeRepository

    @Autowired
    lateinit var customerTypeRepository: CustomerTypeRepository

    @Autowired
    lateinit var accountTypeRepository: AccountTypeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var authToken: UsernamePasswordAuthenticationToken

    @BeforeEach
    fun setup() {
        val personType = personTypeRepository.findById(1).orElseThrow { ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND) }
        val documentType = documentTypeRepository.findById(1).orElseThrow { ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND) }
        val customerType = customerTypeRepository.findById(1).orElseThrow { ApiException(ErrorCode.CUSTOMER_NOT_FOUND) }
        val user = userRepository.findById(1).orElseThrow { ApiException(ErrorCode.USER_NOT_FOUND) }

        customerRepository.save(

            Customer.builder()
                .name1("John")
                .lastname1("Doe")
                .fullname("John Doe")
                .personType(personType)
                .documentType(documentType)
                .documentNumber("33111344")
                .customerType(customerType)
                .user(user)
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

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody),
        ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
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

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody),
        ).andDo(print())
            .andExpect(status().isForbidden())
    }

    @Test
    fun createAccountFailWhenPayloadIsInvalid() {
        val invalidRequestBody = "{}"

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON).content(invalidRequestBody),
        ).andDo(print())
            .andExpect(status().isBadRequest())
    }

    @Test
    fun getAllAccountsSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT)
        createTestAccount(AccountType.CHECKING_ACCOUNT)

        mockMvc.perform(
            get("/api/accounts"),
        ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            // First account data
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].customer").value(1))
            .andExpect(jsonPath("$[0].type").value("SAVINGS ACCOUNT"))
            .andExpect(jsonPath("$[0].currencyIsoCode").value("USD"))
            .andExpect(jsonPath("$[0].bank").value(1))
            .andExpect(jsonPath("$[0].cbu").exists())
            .andExpect(jsonPath("$[0].balance").value(BigDecimal("10000.0")))
            .andExpect(jsonPath("$[0].status").value("PENDING"))
            // Second account data
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].customer").value(1))
            .andExpect(jsonPath("$[1].type").value("CHECKING ACCOUNT"))
            .andExpect(jsonPath("$[1].currencyIsoCode").value("USD"))
            .andExpect(jsonPath("$[1].bank").value(1))
            .andExpect(jsonPath("$[1].cbu").exists())
            .andExpect(jsonPath("$[1].balance").value(BigDecimal("10000.0")))
            .andExpect(jsonPath("$[1].status").value("PENDING"))
    }

    @Test
    fun getAllAccountsFailWhenDatabaseIsEmpty() {
        mockMvc.perform(
            get("/api/accounts"),
        ).andDo(print())
            .andExpect(status().isNotFound())
    }

    @Test
    fun getAccountByIdSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT)

        mockMvc.perform(
            get("/api/accounts/1"),
        ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("customer").value(1))
            .andExpect(jsonPath("type").value("SAVINGS ACCOUNT"))
            .andExpect(jsonPath("currencyIsoCode").value("USD"))
            .andExpect(jsonPath("bank").value(1))
            .andExpect(jsonPath("cbu").exists())
            .andExpect(jsonPath("balance").value(BigDecimal("10000.0")))
            .andExpect(jsonPath("status").value("PENDING"))
    }

    @Test
    fun getAccountByIdFailWhenIdNotFound() {
        mockMvc.perform(
            get("/api/accounts/1"),
        ).andDo(print())
            .andExpect(status().isNotFound())
    }

    @Test
    fun openAccountSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT)

        mockMvc.perform(
            patch("/api/accounts/1/open"),
        ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("operation").value("Opening"))
            .andExpect(jsonPath("entity").value("Account"))
            .andExpect(jsonPath("id").value(1))
            .andExpect(jsonPath("message").value("Account opened!"))
            .andExpect(jsonPath("timestamp").exists())
    }

    @Test
    fun closeAccountSuccessfully() {
        createTestAccount(AccountType.SAVINGS_ACCOUNT)

        mockMvc.perform(
            patch("/api/accounts/1/close"),
        ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("operation").value("Closing"))
            .andExpect(jsonPath("entity").value("Account"))
            .andExpect(jsonPath("id").value(1))
            .andExpect(jsonPath("message").value("Account closed!"))
            .andExpect(jsonPath("timestamp").exists())
    }

    private fun createTestAccount(accountType: AccountType): Account {
        val accountType = accountTypeRepository.findByNameIgnoreCase(accountType.dbName).orElseThrow { ApiException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND) }

        val bankBranch = bankBranchRepository.findByBranchNumberAndBankId(1, 1).orElseThrow { ApiException(ErrorCode.BANK_BRANCH_NOT_FOUND) }

        val currency = currencyRepository.findByIsoCode(CurrencyIsoCodes.USD.name).orElseThrow { ApiException(ErrorCode.CURRENCY_NOT_FOUND) }

        val customer = customerRepository.findById(1).orElseThrow { ApiException(ErrorCode.CUSTOMER_NOT_FOUND) }

        val user = userRepository.findById(1).orElseThrow { ApiException(ErrorCode.USER_NOT_FOUND) }

        val randomCbu = "001000" + (1000000000000000..9999999999999999).random().toString()

        return accountRepository.save(
            Account(
                type = accountType,
                customer = customer,
                currency = currency,
                bank = bankBranch.bank,
                balance = BigDecimal("10000.00"),
                user = user,
                cbu = randomCbu,
            ),
        )
    }
}
