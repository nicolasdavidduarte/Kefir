package mothers

import com.kefir.entities.AmortizationType
import com.kefir.entities.User
import com.kefir.enums.AmortizationTypeName
import java.time.OffsetDateTime

object AmortizationTypeMother {
    fun sample(
        id: Int = 1,
        name: AmortizationTypeName = AmortizationTypeName.FRENCH,
        description: String = "French Amortization",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): AmortizationType = AmortizationType(id, name, description, enabled, user, now, user, now)
}
