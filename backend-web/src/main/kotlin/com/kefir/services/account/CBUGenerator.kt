package com.kefir.services.account

object CBUGenerator {
    fun generate(bank: Int, branch: Int, id: Long): String {
        // 1st block: Bank code (3) + Branch code (4) + Verification number (1)

        var cbu: String =
            bank.toString().padStart(3, '0') +
                branch.toString().padStart(4, '0') +
                (0..9).random().toString()

        // 2nd block: Account number (13) + Verification number (1)
        cbu =
            cbu +
            id.toString().padStart(13, '0') +
            (0..9).random().toString()

        return cbu
    }
}
