package mothers

import com.kefir.entities.Currency
import com.kefir.entities.User
import java.time.OffsetDateTime

object CurrencyMother {
    fun sample(
        id: Int = 1,
        name: String = "TESTO CURRENCY",
        description: String = "Currency Test",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): Currency = Currency(id, name, description, enabled, user, now, user, now)
}
