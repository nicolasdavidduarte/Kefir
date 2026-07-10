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
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_type_id", nullable = false)
  private LoanType loanType;

  @Column(name = "number_of_installments", nullable = false)
  private Integer numberOfInstallments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id", nullable = false)
  private Currency currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "amortization_type_id", nullable = false)
  private AmortizationType amortizationType;

  @Column(name = "annual_interest_rate", nullable = false)
  private BigDecimal annualInterestRate;

  @Column(name = "monthly_interest_rate", nullable = false)
  private BigDecimal monthlyInterestRate;

  @Column(name = "principal_amount", nullable = false)
  private BigDecimal principalAmount;

  @Column(name = "interest_amount", nullable = false)
  private BigDecimal interestAmount;

  @Column(name = "total_operation_amount", nullable = false)
  private BigDecimal totalOperationAmount;

  @Column(name = "opening_date", nullable = false)
  private OffsetDateTime openingDate;

  @Column(name = "expiration_date", nullable = false)
  private OffsetDateTime expirationDate;

  @Column(name = "external_id")
  private Long externalId;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column(name = "status", nullable = false)
  private LoanStatus status = LoanStatus.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @Builder.Default
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by", nullable = false)
  private User updatedBy;

  @Builder.Default
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  @Override
  public String toString() {
    return "{Id: " + this.id + " / Amount: " + this.totalOperationAmount;
  }
}
