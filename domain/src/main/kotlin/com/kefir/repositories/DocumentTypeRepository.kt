package com.kefir.repositories

import com.kefir.entities.DocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTypeRepository : JpaRepository<DocumentType, Int> {

    fun findByNameIgnoreCase(name: String): DocumentType?
}
