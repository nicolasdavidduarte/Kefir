package com.kefir.repositories

import com.kefir.entities.AccountType
import org.springframework.data.jpa.repository.JpaRepository

interface AccountTypeRepository : JpaRepository<AccountType, Int>
