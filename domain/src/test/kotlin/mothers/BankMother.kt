package mothers

import com.kefir.entities.Bank
import com.kefir.entities.User
import com.kefir.enums.BankStatus
import java.time.OffsetDateTime

object BankMother {
    fun sample(
        id: Int = 1,
        name: String = "TESTO BANK",
        status: BankStatus = BankStatus.ACTIVE,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): Bank = Bank(id, name, status, user, now, user, now)
}
