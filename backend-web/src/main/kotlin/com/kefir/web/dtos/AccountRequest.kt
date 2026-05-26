package com.kefir.web.dtos

import com.kefir.entities.AccountType
import com.kefir.entities.Bank
import com.kefir.entities.BankBranch
import com.kefir.entities.Currency
import com.kefir.entities.Customer
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AccountRequest(
    @field:NotNull("Type is required")
    val type: AccountType?,
    @field:NotNull("Customer is required")
    val customer: Customer?,
    @field:NotNull("Currency is required")
    val currency: Currency?,
    @field:NotNull("Bank is required")
    val bank: Bank?,
    @field:NotNull("Bank branch is required")
    val bankBranch: BankBranch?,
    val initialBalance: BigDecimal = BigDecimal.ZERO,
)
