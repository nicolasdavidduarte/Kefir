package com.kefir.services.account

object CBUGenerator {

    private val BLOCK1_WEIGHTS = intArrayOf(7, 1, 3, 9, 7, 1, 3)
    private val BLOCK2_WEIGHTS = intArrayOf(3, 9, 7, 1, 3, 9, 7, 1, 3, 9, 7, 1, 3)

    fun generate(bank: Int, branch: Int, accountNumber: String): String {
        // 1st block: Bank code (3) + Branch code (4) + Verification number (1)

        val block1: String =
            bank.toString().padStart(3, '0') +
                branch.toString().padStart(4, '0')

        val dv1 = calculateDv(block1, BLOCK1_WEIGHTS)

        // 2nd block: Account number (13) + Verification number (1)
        val block2: String =
            accountNumber.padStart(13, '0')

        val dv2 = calculateDv(block2, BLOCK2_WEIGHTS)

        return "$block1$dv1$block2$dv2"
    }

    private fun calculateDv(input: String, weights: IntArray): Int {
        var sum = 0
        for (i in input.indices) {
            sum += Character.getNumericValue(input[i]) * weights[i]
        }
        val remainder = sum % 10
        return if (remainder == 0) 0 else 10 - remainder
    }
}
