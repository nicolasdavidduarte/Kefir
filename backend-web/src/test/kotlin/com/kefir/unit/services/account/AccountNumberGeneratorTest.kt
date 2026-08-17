package com.kefir.unit.services.account

import com.kefir.services.account.AccountNumberGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AccountNumberGeneratorTest {

    @Test
    @DisplayName("Generate account number for account")
    fun generate_account_number_for_account() {
        val accountNumber = AccountNumberGenerator.generate(100, 256456357L)

        assertEquals(13, accountNumber.length)

        assertEquals("100", accountNumber.substring(0..2)) // Account Type
        assertEquals("156582677", accountNumber.substring(3..11)) // Obfuscated Sequence
        assertEquals("9", accountNumber[12].toString()) // Verifier Digit
    }

    @Test
    @DisplayName("Should handle internal DV equal to 0 when remainder is zero")
    fun generateAccountNumberWithZeroVerifierDigit() {
        // sequence = 5L produce base12 = "100272178805" -> 40 -> DV = 0
        val accountNumber = AccountNumberGenerator.generate(accountTypeCode = 100, sequence = 5L)

        assertEquals(13, accountNumber.length)
        assertEquals("0", accountNumber[12].toString())
    }
}
