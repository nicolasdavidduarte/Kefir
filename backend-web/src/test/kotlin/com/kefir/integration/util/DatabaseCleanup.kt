package com.kefir.integration.util

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DatabaseCleanup {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    private val tablesToTruncate = listOf(
        "loan_installment_payment",
        "loan_installment",
        "loan",
        "account",
        "operation_log",
        "customer",
    )

    @Transactional
    fun truncateTestData() {
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate()

        for (tableName in tablesToTruncate) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName RESTART IDENTITY CASCADE").executeUpdate()
        }

        entityManager.clear()
    }
}
