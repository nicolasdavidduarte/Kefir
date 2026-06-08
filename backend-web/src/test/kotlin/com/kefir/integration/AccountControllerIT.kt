package com.kefir.integration

import com.kefir.entities.Customer
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthenticatedUser
import com.kefir.repositories.CustomerRepository
import com.kefir.repositories.CustomerTypeRepository
import com.kefir.repositories.DocumentTypeRepository
import com.kefir.repositories.PersonTypeRepository
import com.kefir.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
class AccountControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Autowired
    lateinit var personTypeRepository: PersonTypeRepository

    @Autowired
    lateinit var documentTypeRepository: DocumentTypeRepository

    @Autowired
    lateinit var customerTypeRepository: CustomerTypeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var authToken: UsernamePasswordAuthenticationToken


    @BeforeEach
    fun setup(){

        val personType = personTypeRepository.findById(1).orElseThrow{ ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND)}
        val documentType = documentTypeRepository.findById(1).orElseThrow{ ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND)}
        val customerType = customerTypeRepository.findById(1).orElseThrow{ ApiException(ErrorCode.CUSTOMER_NOT_FOUND)}
        val user = userRepository.findById(1).orElseThrow{ ApiException(ErrorCode.USER_NOT_FOUND)}

        customerRepository.save(

        Customer.builder()
            .name1("John")
            .lastname1("Doe")
            .personType(personType)
            .documentType(documentType)
            .documentNumber("33111344")
            .customerType(customerType)
            .user(user)
            .build()

        )

        val principal = AuthenticatedUser(
            1,
            "admin"
        )

        authToken = UsernamePasswordAuthenticationToken(
            principal,
            null,
            listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
        )

        SecurityContextHolder.getContext().authentication = authToken
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun createAccountSuccessfully(){

        val requestBody =
            javaClass.classLoader
                .getResource("requests/account/create-account-success.json")
                ?.readText()
                ?: throw IllegalStateException("File not found")

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())


    }

    fun getAllAccountsSuccessfully(){

    }

    fun getAllAccountsFailWhenDatabaseIsEmpty(){

    }

    fun getAccountByIdSuccessfully(){}

    fun getAccountByIdFailWhenIdNotFound(){

    }

    fun openAccountSuccessfully(){}

    fun closeAccountSuccessfully(){}

}