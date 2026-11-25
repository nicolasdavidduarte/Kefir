import com.kefir.entities.Loan
import com.kefir.enums.AccountStatus
import com.kefir.exceptions.DomainErrorCode
import com.kefir.exceptions.DomainException
import mothers.AccountMother
import mothers.AmortizationTypeMother
import mothers.CurrencyMother
import mothers.CustomerMother
import mothers.LoanTypeMother
import mothers.UserMother
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LoanTest {

    @Test
    fun getLoanInstanceWhenCreate() {
        val customer = CustomerMother.sample()
        val currency = CurrencyMother.sample(name = "USD")
        val account = AccountMother.sample(status = AccountStatus.OPENED, currency = currency)
        val loanType = LoanTypeMother.sample()
        val amortizationType = AmortizationTypeMother.sample()
        val principal = BigDecimal.valueOf(5000.00)
        val user = UserMother.sample()

        val loan = Loan.create(
            customer,
            account,
            loanType,
            amortizationType,
            principal,
            currency,
            12,
            1001L,
            user,
        )

        assertNotNull(loan)
        assertEquals(customer, loan.customer)
        assertEquals(account, loan.account)
        assertEquals(currency, loan.currency)
        assertEquals(loanType, loan.loanType)
        assertEquals(12, loan.numberOfInstallments)
    }

    @Test
    fun getExceptionErrorWhenCreateWithCurrencyMismatch() {
        val customer = CustomerMother.sample()
        val accountCurrency = CurrencyMother.sample(id = 1, name = "ARS")
        val loanCurrency = CurrencyMother.sample(id = 2, name = "USD")
        val account = AccountMother.sample(status = AccountStatus.OPENED, currency = accountCurrency)
        val loanType = LoanTypeMother.sample()
        val amortizationType = AmortizationTypeMother.sample()
        val user = UserMother.sample()

        val exception = assertThrows<DomainException> {
            Loan.create(
                customer,
                account,
                loanType,
                amortizationType,
                BigDecimal.valueOf(1000.00),
                loanCurrency,
                6,
                null,
                user,
            )
        }

        assertEquals(DomainErrorCode.CURRENCY_MISMATCH, exception.errorCode)
        assertEquals("Loan currency does not match with the account currency", exception.message)
    }

    @Test
    fun getExceptionErrorWhenCreateWithZeroOrNegativePrincipal() {
        val customer = CustomerMother.sample()
        val currency = CurrencyMother.sample(name = "USD")
        val account = AccountMother.sample(status = AccountStatus.OPENED, currency = currency)
        val loanType = LoanTypeMother.sample()
        val amortizationType = AmortizationTypeMother.sample()
        val user = UserMother.sample()

        val exceptionZero = assertThrows<DomainException> {
            Loan.create(
                customer,
                account,
                loanType,
                amortizationType,
                BigDecimal.ZERO,
                currency,
                6,
                null,
                user,
            )
        }
        assertEquals(DomainErrorCode.INVALID_AMOUNT, exceptionZero.errorCode)

        val exceptionNegative = assertThrows<DomainException> {
            Loan.create(
                customer,
                account,
                loanType,
                amortizationType,
                BigDecimal.valueOf(-100.00),
                currency,
                6,
                null,
                user,
            )
        }
        assertEquals(DomainErrorCode.INVALID_AMOUNT, exceptionNegative.errorCode)
    }

    @Test
    fun getExceptionErrorWhenCreateWithInvalidInstallments() {
        val customer = CustomerMother.sample()
        val currency = CurrencyMother.sample(name = "USD")
        val account = AccountMother.sample(status = AccountStatus.OPENED, currency = currency)
        val loanType = LoanTypeMother.sample()
        val amortizationType = AmortizationTypeMother.sample()
        val user = UserMother.sample()

        val exception = assertThrows<DomainException> {
            Loan.create(
                customer,
                account,
                loanType,
                amortizationType,
                BigDecimal.valueOf(1000.00),
                currency,
                0,
                null,
                user,
            )
        }
        assertEquals(DomainErrorCode.INVALID_INSTALLMENTS, exception.errorCode)
    }
}
