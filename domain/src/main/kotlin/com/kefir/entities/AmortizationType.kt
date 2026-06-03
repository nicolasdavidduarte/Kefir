package com.kefir.entities

import com.kefir.enums.AmortizationTypeName
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "amortization_type")
class AmortizationType(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amortization_type_id_seq_gen")
    @SequenceGenerator(name = "amortization_type_id_seq_gen", sequenceName = "amortization_type_id_seq", allocationSize = 1)
    val id: Int,

    @Enumerated(EnumType.STRING)
    val name: AmortizationTypeName,

    val description: String,

    val enabled: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    val createdAt: OffsetDateTime,

    var updatedAt: OffsetDateTime,
)
