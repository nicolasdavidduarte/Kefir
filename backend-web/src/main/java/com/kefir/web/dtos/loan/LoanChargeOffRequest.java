package com.kefir.web.dtos.loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoanChargeOffRequest(@NotBlank @Size(max = 255) String reason) {}
