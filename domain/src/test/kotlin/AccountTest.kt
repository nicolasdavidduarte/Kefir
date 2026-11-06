import com.kefir.entities.Account
import com.kefir.entities.Loan
import com.kefir.enums.AccountStatus
import com.kefir.enums.LoanStatus
import com.kefir.exceptions.DomainException
import mothers.AccountMother
import mothers.AccountTypeMother
import mothers.BankBranchMother
import mothers.CurrencyMother
import mothers.CustomerMother
import mothers.UserMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.math.BigDecimal
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun getAccountInstanceWhenCreate() {
        val accountType = AccountTypeMother.sample()
        val customer = CustomerMother.sample()
        val currency = CurrencyMother.sample()
        val bankBranch = BankBranchMother.sample()
        val initialBalance = BigDecimal.valueOf(10000.00)
        val accountNumber = "123456789"
        val cbu = "987654321"
        val user = UserMother.sample()
        val account = Account.create(accountType, customer, currency, bankBranch, initialBalance, user, accountNumber, cbu)

        assertEquals(accountType, account.type)
        assertEquals(customer, account.customer)
        assertEquals(currency, account.currency)
        assertEquals(bankBranch.bank, account.bank)
        assertEquals(initialBalance, account.balance)
        assertEquals(cbu, account.cbu)
        assertEquals(user, account.createdBy)
        assertEquals(user, account.updatedBy)
    }

    @Test
    fun getExceptionErrorWhenCreateWithNegativeInitialBalance() {
        val accountType = AccountTypeMother.sample()
        val customer = CustomerMother.sample()
        val currency = CurrencyMother.sample()
        val bankBranch = BankBranchMother.sample()
        val initialBalance = BigDecimal.valueOf(-5000.00)
        val accountNumber = "123456789"
        val cbu = "987654321"
        val user = UserMother.sample()

        val exception = assertThrows<DomainException> {
            Account.create(accountType, customer, currency, bankBranch, initialBalance, user, accountNumber, cbu)
        }

        assertEquals("Amount must be positive", exception.message)
    }

    @Test
    fun accountStatusUpdatedWhenOpened() {
        val account = AccountMother.sample(status = AccountStatus.PENDING)
        val user = UserMother.sample()

        account.open(user)

        assertEquals(AccountStatus.OPENED, account.status)
        assertEquals(user, account.updatedBy)
    }

    @Test
    fun accountStatusUpdatedWhenSuspendedWithAllowedStatus() {
        val account = AccountMother.sample(status = AccountStatus.OPENED)
        val user = UserMother.sample()

        account.suspend(user)

        assertEquals(AccountStatus.SUSPENDED, account.status)
        assertEquals(user, account.updatedBy)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED"], mode = EnumSource.Mode.EXCLUDE)
    fun accountStatusThrowsErrorWhenSuspendedWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val user = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.suspend(user)
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED", "PENDING"], mode = EnumSource.Mode.INCLUDE)
    fun accountStatusUpdatedWhenClosedWithAllowedStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val user = UserMother.sample()
        val loans = listOf<Loan>()

        account.close(user, loans)

        assertEquals(AccountStatus.CLOSED, account.status)
        assertEquals(user, account.updatedBy)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED", "PENDING"], mode = EnumSource.Mode.EXCLUDE)
    fun accountStatusThrowsErrorWhenClosedWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val user = UserMother.sample()
        val loans = listOf<Loan>()

        val exception = assertThrows<DomainException> {
            account.close(user, loans)
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["PENDING"], mode = EnumSource.Mode.EXCLUDE)
    fun accountStatusThrowsErrorWhenOpenedWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val user = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.open(user)
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @Test
    fun accountThrowsErrorWhenClosedWithActiveLoans() {
        val account = AccountMother.sample(status = AccountStatus.OPENED)
        val user = UserMother.sample()
        val loans = listOf(Loan.builder().status(LoanStatus.ACTIVE).build())

        val exception = assertThrows<DomainException> {
            account.close(user, loans)
        }

        assertEquals("Account is not valid for closure", exception.message)
    }

    @Test
    fun accountIsValidWhenUsedToCreateLoanWithOpenedStatus() {
        val account = AccountMother.sample(status = AccountStatus.OPENED)

        account.validateCanBeUsedToCreateLoan()
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED"], mode = EnumSource.Mode.EXCLUDE)
    fun accountThrowsErrorWhenUsedToCreateLoanWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)

        val exception = assertThrows<DomainException> {
            account.validateCanBeUsedToCreateLoan()
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @Test
    fun accountBalanceUpdatedWhenAddBalanceWithOpenedStatus() {
        val account = AccountMother.sample(status = AccountStatus.OPENED, balance = BigDecimal.valueOf(1000.00))
        val amount = BigDecimal.valueOf(500.00)
        val updatedBy = UserMother.sample()

        account.addBalance(amount, updatedBy)

        assertEquals(BigDecimal.valueOf(1500.00), account.balance)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED"], mode = EnumSource.Mode.EXCLUDE)
    fun accountThrowsErrorWhenAddBalanceWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val amount = BigDecimal.valueOf(500.00)
        val updatedBy = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.addBalance(amount, updatedBy)
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @Test
    fun accountThrowsErrorWhenAddBalanceWithNegativeAmount() {
        val account = AccountMother.sample(status = AccountStatus.OPENED)
        val amount = BigDecimal.valueOf(-100.00)
        val updatedBy = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.addBalance(amount, updatedBy)
        }

        assertEquals("Amount must be positive", exception.message)
    }

    @Test
    fun accountBalanceUpdatedWhenSubtractBalanceWithOpenedStatus() {
        val account = AccountMother.sample(status = AccountStatus.OPENED, balance = BigDecimal.valueOf(1000.00))
        val amount = BigDecimal.valueOf(400.00)
        val updatedBy = UserMother.sample()

        account.subtractBalance(amount, updatedBy)

        assertEquals(BigDecimal.valueOf(600.00), account.balance)
    }

    @ParameterizedTest
    @EnumSource(AccountStatus::class, names = ["OPENED"], mode = EnumSource.Mode.EXCLUDE)
    fun accountThrowsErrorWhenSubtractBalanceWithWrongStatus(status: AccountStatus) {
        val account = AccountMother.sample(status = status)
        val amount = BigDecimal.valueOf(100.00)
        val updatedBy = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.subtractBalance(amount, updatedBy)
        }

        assertEquals("Account is not in a valid state", exception.message)
    }

    @Test
    fun accountThrowsErrorWhenSubtractBalanceWithNegativeAmount() {
        val account = AccountMother.sample(status = AccountStatus.OPENED)
        val amount = BigDecimal.valueOf(-100.00)
        val updatedBy = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.subtractBalance(amount, updatedBy)
        }

        assertEquals("Amount must be positive", exception.message)
    }

    @Test
    fun accountThrowsErrorWhenSubtractBalanceWithInsufficientFunds() {
        val account = AccountMother.sample(status = AccountStatus.OPENED, balance = BigDecimal.valueOf(100.00))
        val amount = BigDecimal.valueOf(200.00)
        val updatedBy = UserMother.sample()

        val exception = assertThrows<DomainException> {
            account.subtractBalance(amount, updatedBy)
        }

        assertEquals("Account does not have the necessary funds to complete the operation", exception.message)
    }
}
