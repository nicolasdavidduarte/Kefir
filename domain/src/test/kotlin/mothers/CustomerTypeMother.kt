package mothers

import com.kefir.entities.CustomerType
import com.kefir.entities.User
import java.time.OffsetDateTime

object CustomerTypeMother {
    fun sample(
        id: Int = 1,
        name: String = "Test",
        description: String = "Test",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): CustomerType = CustomerType(id, name, description, enabled, user, now, user, now)
}
