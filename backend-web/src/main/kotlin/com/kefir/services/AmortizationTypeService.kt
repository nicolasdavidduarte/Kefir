package com.kefir.services

import com.kefir.entities.AmortizationType
import com.kefir.enums.AmortizationTypeName
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.AmortizationTypeRepository
import org.springframework.stereotype.Service

@Service
class AmortizationTypeService(private val amortizationTypeRepository: AmortizationTypeRepository) {

    fun getByNameIgnoringCase(amortizationTypeName: AmortizationTypeName): AmortizationType = amortizationTypeRepository.findByName(amortizationTypeName)
        .orElseThrow { throw ApiException(ErrorCode.AMORTIZATION_TYPE_NOT_FOUND) }
}
