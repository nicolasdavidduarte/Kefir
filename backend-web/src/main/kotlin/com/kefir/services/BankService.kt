package com.kefir.services

import com.kefir.entities.Bank
import com.kefir.entities.enable
import com.kefir.exceptions.BankNotFoundException
import com.kefir.repositories.BankRepository
import com.kefir.web.DTOs.EntityApprovalResponse
import com.kefir.web.DTOs.BankRequest
import com.kefir.web.DTOs.BankResponse
import com.kefir.web.DTOs.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class BankService(val bankRepository: BankRepository) {

    fun findAll() : List<BankResponse> = bankRepository.findAll().
                                map(Bank::toResponse).
                                toList().
                                ifEmpty { throw BankNotFoundException("No banks found") }


    fun create(bankRequest: BankRequest) : BankResponse = bankRepository.save(Bank(name = requireNotNull(bankRequest.name))).toResponse()

    fun approve(id: Long) : EntityApprovalResponse {
        val bank = bankRepository.findById(id).orElseThrow{BankNotFoundException("Bank with id $id not found")}
        bank.enable()
        bankRepository.save(bank)

        return EntityApprovalResponse("Bank approved and enabled!", "Bank", id, LocalDateTime.now())
    }
}