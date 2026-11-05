package mothers

import com.kefir.entities.Role

object RoleMother {
    fun sample(
        name: String = "ROLE_TEST",
    ): Role = Role(name)
}
