package com.kefir.unit.services.account

import com.kefir.services.account.CBUGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CBUGeneratorTest {

    @Test
    @DisplayName("Generate CBU for account")
    fun generate_cbu_for_account() {
        val cbu = CBUGenerator.generate(bank = 321, branch = 123, id = 987L)

        assert(cbu.length == 22)

        // RegEx use because of the random security numbers
        val cbuPattern = Regex("^3210123\\d0000000000987\\d$")
        assert(cbu.matches(cbuPattern)) { "CBU '$cbu' did not match the expected format" }
    }
}
