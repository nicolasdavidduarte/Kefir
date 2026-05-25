package com.kefir.enums

enum class LoanInstallmentStatus(val id: Long) {
    PAYMENT_PENDING(5),
    PAID(6),
    OVERDUE(7),
    CANCELLED(8),
}
