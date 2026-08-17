package com.kefir.unit.services.account

import com.kefir.services.account.CBUGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CBUGeneratorTest {

    @Test
    @DisplayName("Generate CBU for account")
    fun generate_cbu_for_account() {
        val cbu = CBUGenerator.generate(bank = 2, branch = 12, accountNumber = "9871235756121")

        assertEquals(22, cbu.length)

        assertEquals("002", cbu.substring(0..2)) // Bank
        assertEquals("0012", cbu.substring(3..6)) // Branch
        assertEquals("7", cbu[7].toString()) // 1st Verifier Digit
        assertEquals("9871235756121", cbu.substring(8..20)) // Account
        assertEquals("5", cbu[21].toString()) // 2nd Verifier Digit
    }

    @Test
    @DisplayName("Should handle DV equal to 0 when remainder is zero")
    fun generateCbuWithZeroVerifierDigits() {
        // bank=1, branch=9 produce -> DV1 = 0
        // account="0000000001007" produce -> DV2 = 0
        val cbu = CBUGenerator.generate(bank = 1, branch = 9, accountNumber = "0000000001007")

        assertEquals("0", cbu[7].toString())
        assertEquals("0", cbu[21].toString())
    }
}
