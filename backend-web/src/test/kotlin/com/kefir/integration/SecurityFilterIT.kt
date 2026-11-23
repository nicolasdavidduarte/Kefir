package com.kefir.integration

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

class SecurityFilterIT : SecuredIntegrationTestBase() {

    @Test
    fun requestWithoutTokenReturns401() {
        mockMvc.get("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun requestWithMalformedTokenReturns401() {
        mockMvc.get("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer this.is.not.a.valid.jwt")
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    fun viewerCannotCreateCustomerReturns403() {
        val token = loginAndGetToken("viewer", "viewer")

        val body = javaClass.classLoader
            .getResource("requests/customer/create-customer-success.json")
            ?.readText() ?: error("File not found")

        mockMvc.post("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun operatorCannotAccessActuatorAdminEndpointsReturns403() {
        val token = loginAndGetToken("operator", "operator")

        mockMvc.get("/actuator/metrics") {
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun operatorWithValidJwtCanListAccounts() {
        val token = loginAndGetToken("operator", "operator")

        mockMvc.get("/api/accounts") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun idempotentPostReplayReturnsCachedResponseWithoutDuplicateSideEffect() {
        val token = loginAndGetToken("operator", "operator")
        val idempotencyKey = UUID.randomUUID().toString()

        val body = javaClass.classLoader
            .getResource("requests/customer/create-customer-success.json")
            ?.readText() ?: error("File not found")

        // First call — creates the customer
        val firstResult = mockMvc.post("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", "Bearer $token")
            header("Idempotency-Key", idempotencyKey)
        }.andExpect {
            status { isCreated() }
        }.andReturn()

        val firstResponse = firstResult.response.contentAsString

        // Second call — same key, same body → must return exactly the same response (cached)
        val secondResult = mockMvc.post("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", "Bearer $token")
            header("Idempotency-Key", idempotencyKey)
        }.andExpect {
            // The idempotency filter replays the original 201 Created response
            status { isCreated() }
        }.andReturn()

        val secondResponse = secondResult.response.contentAsString

        // Both responses must carry identical content (the cached body)
        assert(firstResponse == secondResponse) {
            "Expected idempotent replay to return same body.\n" +
                "First:  $firstResponse\n" +
                "Second: $secondResponse"
        }
    }

    @Test
    fun idempotentPostReplayWithDifferentBodyReturns409() {
        val token = loginAndGetToken("operator", "operator")
        val idempotencyKey = UUID.randomUUID().toString()

        val originalBody = javaClass.classLoader
            .getResource("requests/customer/create-customer-success.json")
            ?.readText() ?: error("File not found")

        // First call — succeeds
        mockMvc.post("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = originalBody
            header("Authorization", "Bearer $token")
            header("Idempotency-Key", idempotencyKey)
        }.andExpect {
            status { isCreated() }
        }

        val differentBody = javaClass.classLoader
            .getResource("requests/customer/update-customer-success.json")
            ?.readText() ?: error("File not found")

        // Second call — same key, DIFFERENT body → must be rejected
        mockMvc.post("/api/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = differentBody
            header("Authorization", "Bearer $token")
            header("Idempotency-Key", idempotencyKey)
        }.andExpect {
            status { isConflict() }
            jsonPath("$.message") { value("Idempotency-Key reuse with different request body") }
        }
    }
}
