package com.kefir.repositories

import com.kefir.entities.AccountType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AccountTypeRepository : JpaRepository<AccountType, Int> {

    fun findByNameIgnoreCase(name: String?): Optional<AccountType>
}
