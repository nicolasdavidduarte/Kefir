package com.kefir.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "loan")
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customer;
  private Integer loanType;
  private Double totalOperationAmount;
  private LocalDate openingDate;
  private Integer currency;
  private LocalDate expirationDate;
  private Integer numberOfInstallments;
  private LocalDate closedDate;
  private Integer closedCode;
  private LocalDate nextInstallmentDate;
  private Integer status;
  private LocalDate lastModificationDate;
  private Integer coreUser;
  private Long externalId;

  @Override
  public String toString() {
    return "{Id: "
        + this.id
        + " / Amount: "
        + this.totalOperationAmount
        + " / first payment due date: "
        + this.nextInstallmentDate
        + " }";
  }
}
