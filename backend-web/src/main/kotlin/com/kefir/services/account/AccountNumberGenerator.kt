package com.kefir.services.account

object AccountNumberGenerator {

    private const val MULTIPLIER = 2654435761L
    private const val MOD_VAL = 1_000_000_000L

    fun generate(accountTypeCode: Int, sequence: Long): String {
        val obfuscatedSeq = (sequence * MULTIPLIER) % MOD_VAL

        val base12 = "%03d%09d".format(accountTypeCode, obfuscatedSeq)

        val dv = calculateAccountDV(base12)

        return "$base12$dv"
    }

    private fun calculateAccountDV(input: String): Int {
        val weights = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1) // Mod 10 with weight sequence (1,2)
        var sum = 0
        for (i in input.indices) {
            val digit = Character.getNumericValue(input[i]) * weights[i]
            sum += if (digit > 9) digit - 9 else digit
        }
        val remainder = sum % 10
        return if (remainder == 0) 0 else 10 - remainder
    }
}
