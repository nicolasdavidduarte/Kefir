package com.kefir.web.dtos.loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountSuspensionRequest(@NotBlank @Size(max = 255) String reason) {}
