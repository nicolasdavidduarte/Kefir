package com.kefir.web.dtos.account

import com.kefir.enums.AccountType
import com.kefir.enums.CurrencyIsoCodes
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AccountRequest(
    @field:NotNull("Account type is required")
    val type: AccountType?,
    @field:NotNull("Customer id is required")
    val customerId: Long?,
    @field:NotNull("Currency id is required")
    val currencyIsoCode: CurrencyIsoCodes?,
    @field:NotNull("Bank id is required")
    val bankId: Int?,
    @field:NotNull("Bank branch id is required")
    val bankBranchId: Int?,
    val initialBalance: BigDecimal = BigDecimal.ZERO,
)
