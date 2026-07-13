package com.kefir.repositories

import com.kefir.entities.PaymentMethod
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PaymentMethodRepository : JpaRepository<PaymentMethod, Long> {
    fun findByName(name: String): Optional<PaymentMethod>
}
