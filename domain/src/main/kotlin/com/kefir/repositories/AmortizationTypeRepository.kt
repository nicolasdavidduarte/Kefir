package com.kefir.repositories

import com.kefir.entities.AmortizationType
import com.kefir.enums.AmortizationTypeName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AmortizationTypeRepository : JpaRepository<AmortizationType, Int> {
    fun findByName(name: AmortizationTypeName): Optional<AmortizationType>
}
