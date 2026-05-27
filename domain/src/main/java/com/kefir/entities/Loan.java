package com.kefir.entities;

import com.kefir.enums.LoanStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_id_seq_gen")
  @SequenceGenerator(name = "loan_id_seq_gen", sequenceName = "loan_id_seq", allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_type_id", nullable = false)
  private LoanType loanType;

  @Column(name = "number_of_installments", nullable = false)
  private Integer numberOfInstallments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id", nullable = false)
  private Currency currency;

  private BigDecimal totalOperationAmount;

  private OffsetDateTime openingDate;

  private OffsetDateTime expirationDate;

  private Long externalId;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column(name = "status", nullable = false)
  private LoanStatus status = LoanStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private CoreUser user;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Override
  public String toString() {
    return "{Id: " + this.id + " / Amount: " + this.totalOperationAmount;
  }
}
