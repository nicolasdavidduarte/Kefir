package com.kefir.services

import com.kefir.entities.Bank
import com.kefir.entities.enable
import com.kefir.exceptions.ApiException
import com.kefir.exceptions.ErrorCode
import com.kefir.infrastructure.security.AuthService
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
    private val bankRepository: BankRepository,
    private val userService: UserService,
    private val authService: AuthService,
) {
    fun getAll(): List<BankResponse> = bankRepository
        .findAll()
        .map(Bank::toResponse)
        .toList()
        .ifEmpty { throw ApiException(ErrorCode.BANK_NOT_FOUND) }

    fun create(bankRequest: BankRequest): BankResponse = bankRepository.save(
        Bank(
            name = requireNotNull(bankRequest.name),
            user = userService.getById(authService.currentUserId),
        ),
    ).toResponse()

    fun enable(id: Int): EntityOperationResponse {
        val bank = bankRepository.findById(id).orElseThrow { ApiException(ErrorCode.BANK_NOT_FOUND) }
        bank.enable()
        bankRepository.save(bank)

        return EntityOperationResponse(operation = "Enable", entity = "Bank", id = id.toLong(), message = "Bank enabled!", timestamp = LocalDateTime.now())
    }
}
