package com.kefir.integration

import com.kefir.integration.util.DatabaseCleanup
import com.kefir.repositories.LoanRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @Autowired private lateinit var loanRepository: LoanRepository

    companion object {
        val postgres = PostgreSQLContainer("postgres:16-alpine").apply {
            withDatabaseName("kefir_test")
            withUsername("test")
            withPassword("test")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName)
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        databaseCleanup.truncateTestData()

        println("Loans after cleanup: " + loanRepository.count())
    }
}
