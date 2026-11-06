package mothers

import com.kefir.entities.Role
import com.kefir.entities.User
import java.time.OffsetDateTime

object UserMother {
    fun sample(
        id: Int = 1,
        username: String = "TEST_USER",
        password: String = "password",
        fullName: String = "Test User Name",
        enabled: Boolean = true,
        user: User? = null,
        now: OffsetDateTime = OffsetDateTime.now(),
        roles: Set<Role> = setOf(RoleMother.sample()),
    ): User = User(id, username, password, fullName, enabled, user, now, user, now, roles)
}
