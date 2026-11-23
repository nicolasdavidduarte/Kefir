package com.kefir.integration

import com.jayway.jsonpath.JsonPath
import com.kefir.integration.util.DatabaseCleanup
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.postgresql.PostgreSQLContainer

@SpringBootTest
@AutoConfigureMockMvc // filters ON — the default; addFilters = true
@ActiveProfiles("test")
abstract class SecuredIntegrationTestBase {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

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
    }

    fun loginAndGetToken(username: String, password: String): String {
        val loginBody = """{"username":"$username","password":"$password"}"""

        val result = mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = loginBody
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val json = result.response.contentAsString
        return JsonPath.read(json, "$.accessToken")
    }

    fun org.springframework.test.web.servlet.MockHttpServletRequestDsl.bearerToken(token: String) {
        header("Authorization", "Bearer $token")
    }
}
