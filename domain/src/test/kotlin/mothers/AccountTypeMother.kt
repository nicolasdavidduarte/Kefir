package mothers

import com.kefir.entities.AccountType
import com.kefir.entities.User
import java.time.OffsetDateTime

object AccountTypeMother {
    fun sample(
        id: Int = 1,
        name: String = "Test",
        code: Int = 1,
        description: String = "Test",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): AccountType = AccountType(id, name, code, description, enabled, user, now, user, now)
}
