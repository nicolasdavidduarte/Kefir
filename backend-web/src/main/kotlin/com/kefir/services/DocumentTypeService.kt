package com.kefir.services

import com.kefir.enums.DocumentType
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.DocumentTypeRepository
import org.springframework.stereotype.Service

@Service
class DocumentTypeService(private val documentTypeRepository: DocumentTypeRepository) {

    fun getByName(name: DocumentType) = documentTypeRepository.findByNameIgnoreCase(name.name).orElseThrow { throw ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND) }
}
