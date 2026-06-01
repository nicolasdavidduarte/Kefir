package com.kefir.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime

@Entity
@Table(name = "loan_type")
class LoanType(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "loan_type_seq_gen",
    )
    @SequenceGenerator(
        name = "loan_type_seq_gen",
        sequenceName = "loan_type_id_seq",
        allocationSize = 1,
    )
    val id: Int = 0,

    @field:NotBlank(message = "Name is mandatory")
    val name: String,

    @Column(name = "description", nullable = true, updatable = true)
    val description: String,

    @Column(name = "enabled", nullable = false, updatable = true)
    var enabled: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false, updatable = true)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    companion object {
        @JvmStatic
        fun createNew(
            name: String,
            description: String,
            user: User,
        ): LoanType = LoanType(
            name = name,
            description = description,
            user = user,
        )
    }
}
