package com.kefir.repositories

import com.kefir.entities.LoanInstallment
import com.kefir.entities.LoanInstallmentPayment
import com.kefir.entities.PaymentMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByName(name: String): Optional<PaymentMethod>
}
