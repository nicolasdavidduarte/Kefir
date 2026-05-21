package com.kefir.unit

import com.kefir.services.aux.account.CBUGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CBUGeneratorTest {

    @Test
    @DisplayName("Generate CBU for account")
    fun generate_cbu_for_account() {
        val cbu = CBUGenerator.generateCBU(bank = 321L, branch = 123L, id = 987L)

        assert(cbu.length == 22)

        // RegEx use because of the random security numbers
        val cbuPattern = Regex("^3210123\\d0000000000987\\d$")
        assert(cbu.matches(cbuPattern)) { "CBU '$cbu' did not match the expected format" }
    }
}
