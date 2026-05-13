package com.kefir.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator

import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
class Account(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq_gen")
    @SequenceGenerator(name = "account_id_seq_gen", sequenceName = "account_id_seq", allocationSize = 1)
    var id: Long = 0,
    val customerId: Long,
    val currencyId: Long,
    val balance: BigDecimal = BigDecimal.ZERO,
    val status: String)