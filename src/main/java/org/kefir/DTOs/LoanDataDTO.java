package org.kefir.DTOs;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Value
@Builder
public class LoanDataDTO {
    Long id;
    Integer customer;
    Integer loanType;
    Double totalOperationAmount;
    Date openingDate;
    Integer currency;
    Date expirationDate;
    Integer totalTermDays;
    Date closedDate;
    Integer closedCode;
    Date nextInstallmentDate;
    Integer status;
    Date lastModificationDate;
    Integer coreUser;
}
