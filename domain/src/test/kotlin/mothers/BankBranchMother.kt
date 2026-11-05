package mothers

import com.kefir.entities.Bank
import com.kefir.entities.BankBranch
import com.kefir.entities.User
import com.kefir.enums.BankStatus
import java.time.OffsetDateTime

object BankBranchMother {
    fun sample(
        id: Int = 1,
        branchNumber: Int = 1,
        bank: Bank = BankMother.sample(),
        name: String = "BANK BRANCH TEST",
        address: String = "TESTO AV. 123",
        status: BankStatus = BankStatus.ACTIVE,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): BankBranch = BankBranch(id, branchNumber, bank, name, address, status, user, now, user, now)
}
