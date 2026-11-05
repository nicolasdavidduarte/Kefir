package mothers

import com.kefir.entities.PersonType
import com.kefir.entities.User
import java.time.OffsetDateTime

object PersonTypeMother {
    fun sample(
        id: Int = 1,
        name: String = "Test",
        description: String = "Test",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): PersonType = PersonType(id, name, description, enabled, user, now, user, now)
}
