package mothers

import com.kefir.entities.LoanType
import com.kefir.entities.User
import java.math.BigDecimal
import java.time.OffsetDateTime

object LoanTypeMother {
    fun sample(
        id: Int = 1,
        name: String = "PERSONAL",
        description: String = "Personal Loan",
        annualInterestRate: BigDecimal = BigDecimal.valueOf(0.12),
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): LoanType = LoanType(id, name, description, annualInterestRate, enabled, user, now, user, now)
}
