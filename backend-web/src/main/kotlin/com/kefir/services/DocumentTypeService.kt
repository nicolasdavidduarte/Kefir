package com.kefir.services

import com.kefir.enums.DocumentType
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.repositories.DocumentTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentTypeService(private val documentTypeRepository: DocumentTypeRepository) {

    @Transactional(readOnly = true)
    fun getByName(name: DocumentType): com.kefir.entities.DocumentType = documentTypeRepository.findByNameIgnoreCase(name.name).orElseThrow { throw ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND) }
}
