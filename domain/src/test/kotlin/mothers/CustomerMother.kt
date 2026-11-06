package mothers

import com.kefir.entities.Customer
import com.kefir.entities.CustomerType
import com.kefir.entities.DocumentType
import com.kefir.entities.PersonType
import com.kefir.entities.User
import com.kefir.enums.CustomerStatus
import java.time.OffsetDateTime

object CustomerMother {
    fun sample(
        id: Long = 1L,
        name1: String = "Testo",
        name2: String? = null,
        name3: String? = null,
        lastname1: String = "Test",
        lastname2: String? = null,
        lastname3: String? = null,
        fullname: String = "Testo Test",
        personType: PersonType = PersonTypeMother.sample(),
        documentType: DocumentType = DocumentTypeMother.sample(),
        documentNumber: String = "123456789",
        customerType: CustomerType = CustomerTypeMother.sample(),
        email: String = "testo@test.com.org",
        status: CustomerStatus = CustomerStatus.ACTIVE,
        user: User = UserMother.sample(),
        now: OffsetDateTime = OffsetDateTime.now(),
    ): Customer = Customer(id, name1, name2, name3, lastname1, lastname2, lastname3, fullname, personType, documentType, documentNumber, customerType, email, status, user, now, user, now)
}
