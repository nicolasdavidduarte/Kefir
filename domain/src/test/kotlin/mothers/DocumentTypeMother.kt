package mothers

import com.kefir.entities.DocumentType
import com.kefir.entities.User
import java.time.OffsetDateTime

object DocumentTypeMother {
    fun sample(
        id: Int = 1,
        name: String = "Test",
        enabled: Boolean = true,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): DocumentType = DocumentType(id, name, enabled, user, now, user, now)
}
