package com.kefir.services

import com.kefir.entities.Bank
import com.kefir.entities.enable
import com.kefir.exceptions.BankNotFoundException
import com.kefir.repositories.BankRepository
import com.kefir.web.dtos.BankRequest
import com.kefir.web.dtos.BankResponse
import com.kefir.web.dtos.EntityOperationResponse
import com.kefir.web.dtos.toResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class BankService(
    val bankRepository: BankRepository,
    val auxAuthService: AuxAuthService,
) {
    fun getAll(): List<BankResponse> = bankRepository
        .findAll()
        .map(Bank::toResponse)
        .toList()
        .ifEmpty { throw BankNotFoundException() }

    fun create(bankRequest: BankRequest): BankResponse = bankRepository.save(
        Bank(
            name = requireNotNull(bankRequest.name),
            user = auxAuthService.getUserFromAuth(),
        ),
    ).toResponse()

    fun enable(id: Int): EntityOperationResponse {
        val bank = bankRepository.findById(id).orElseThrow { BankNotFoundException() }
        bank.enable()
        bankRepository.save(bank)

        return EntityOperationResponse(operation = "Enable", entity = "Bank", id = id.toLong(), message = "Bank enabled!", timestamp = LocalDateTime.now())
    }
}
