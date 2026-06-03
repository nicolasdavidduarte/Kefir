package com.kefir.services

import com.kefir.entities.AmortizationType
import com.kefir.enums.AmortizationTypeName
import com.kefir.exceptions.AmortizationTypeNotFoundException
import com.kefir.repositories.AmortizationTypeRepository
import org.springframework.stereotype.Service

@Service
class AmortizationTypeService(val amortizationTypeRepository: AmortizationTypeRepository) {

    fun getByNameIgnoringCase(amortizationTypeName: AmortizationTypeName): AmortizationType = amortizationTypeRepository.findByName(amortizationTypeName)
        .orElseThrow { throw AmortizationTypeNotFoundException() }
}
