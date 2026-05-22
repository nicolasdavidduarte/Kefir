package com.kefir.services

import com.kefir.entities.Bank
import com.kefir.exceptions.BankNotFoundException
import com.kefir.repositories.BankRepository
import com.kefir.web.dtos.BankRequest
import com.kefir.web.dtos.BankResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BankService(
    val bankRepository: BankRepository,
) {
    fun findAll(): List<BankResponse> = bankRepository
        .findAll()
        .map(Bank::toResponse)
        .toList()
        .ifEmpty { throw BankNotFoundException("No banks found") }

    fun create(bankRequest: BankRequest): BankResponse = bankRepository.save(Bank(name = requireNotNull(bankRequest.name))).toResponse()

//    fun approve(id: Long): EntityOperationResponse {
//        val bank = bankRepository.findById(id).orElseThrow { BankNotFoundException("Bank with id $id not found") }
//        bank.enable()
//        bankRepository.save(bank)
//
//        return EntityOperationResponse("Bank approved and enabled!", "Bank", id, LocalDateTime.now())
//    }
}
