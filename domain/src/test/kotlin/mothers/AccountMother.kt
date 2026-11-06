package mothers

import com.kefir.entities.Account
import com.kefir.entities.AccountType
import com.kefir.entities.Bank
import com.kefir.entities.Currency
import com.kefir.entities.Customer
import com.kefir.entities.User
import com.kefir.enums.AccountStatus
import java.math.BigDecimal
import java.time.OffsetDateTime

object AccountMother {
    fun sample(
        id: Long = 1L,
        type: AccountType = AccountTypeMother.sample(),
        customer: Customer = CustomerMother.sample(),
        currency: Currency = CurrencyMother.sample(),
        bank: Bank = BankMother.sample(),
        accountNumber: String = "123456789",
        cbu: String = "987654321",
        balance: BigDecimal = BigDecimal.valueOf(1000.00),
        status: AccountStatus,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): Account = Account(id, type, customer, currency, bank, accountNumber, cbu, balance, status, user, now, user, now)
}
