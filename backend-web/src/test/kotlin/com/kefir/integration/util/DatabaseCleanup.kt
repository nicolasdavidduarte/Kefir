package com.kefir.integration.util

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DatabaseCleanup {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    // The precise list of business tables that accumulate test data
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
        // Defer all foreign key constraints until the end of the transaction
        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate()

        // Execute truncation and reset primary key counters to 1
        for (tableName in tablesToTruncate) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName RESTART IDENTITY CASCADE").executeUpdate()
        }
    }
}
