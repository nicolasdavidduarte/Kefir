package com.kefir.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "loan_type")
class LoanType(
    @Id
    @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "loan_type_seq_gen")
    @SequenceGenerator(
        name = "loan_type_seq_gen",
        sequenceName = "loan_type_id_seq",
        allocationSize = 1)
    var id : Long,
    @field:NotBlank(message = "Name is mandatory")
    val name: String,
    val description: String?,
    val status: Int) {
}